package co.lemnisk.consumer.service.s3;


import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.common.exception.TransformerExceptionHandler;
import co.lemnisk.common.metrics.EventMonitoring;
import co.lemnisk.common.tracing.TraceHelper;
import co.lemnisk.common.tracing.Tracing;
import co.lemnisk.common.tracing.TracingConstant;
import co.lemnisk.consumer.dto.MessageDTO;
import co.lemnisk.consumer.util.FileUtils;
import co.lemnisk.consumer.util.S3FileProcessingConstants;
import io.opentelemetry.api.trace.Span;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class S3MessageLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(S3MessageLogService.class);

    private static final String random = UUID.randomUUID().toString().substring(0, 5);

    private static final long MAX_FILE_SIZE_MB = 1024 * 1024 * 30;

    @Autowired
    EventMonitoring eventMonitoring;

    @Autowired
    Tracing tracing;

    @Autowired
    TransformerExceptionHandler transformerExceptionHandler;

    public static final String TRANSFORMED_PATH = "$.transformedPayload";

    public static Path createFileWithDir(String directory, String filename) {
        File dir = new File(directory);
        if (!dir.exists()) dir.mkdirs();
        return Paths.get(directory + File.separatorChar + filename);
    }


    private String getDirectoryStructureByDestinationSource(int destinationId, int sourceId, String processingStage){

        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month =  calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        StringBuilder directoryPath = new StringBuilder();

            directoryPath.append(S3FileProcessingConstants.DESTINATION_SENDER_LOG_ROOT)
                         .append("/")
                         .append(processingStage)
                         .append("/")
                         .append(destinationId)
                         .append("/")
                         .append(sourceId)
                         .append("/")
                         .append(year)
                         .append("/")
                         .append(month)
                         .append("/")
                         .append(day)
                         .append("/")
                         .append(hour)
                         .append("/");

        return directoryPath.toString();
    }

    private String getFileNameForDestinationSender(int destinationId, int sourceId) {

        String randomString= random;

        StringBuilder filePath = new StringBuilder();
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH").format(new Date());

        filePath.append("logs-").
                append(timeStamp)
                .append("-").
                append(destinationId)
                .append("-")
                .append(sourceId)
                .append("-")
                .append(randomString)
                .append(".log");

        return filePath.toString();
    }

    private void writeMessageToFile(String message,int destinationInstanceId, int sourceId) throws IOException {

        String directoryPath = getDirectoryStructureByDestinationSource(destinationInstanceId,sourceId,"PROCESSING");
        String fileName = getFileNameForDestinationSender(destinationInstanceId,sourceId);
        Path path = createFileWithDir(directoryPath,fileName);

        try{
            File file = new File(path.toString());
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {
                if (file.length() < MAX_FILE_SIZE_MB) {
                    writer.write(message);
                    writer.write(System.lineSeparator());
                    writer.flush();
                }
                else {
                    writer.write(message);
                    writer.write(System.lineSeparator());
                    writer.flush();
                    File completedFile = new File(directoryPath + "/" + FileUtils.getCompletedFileNameFromOriginalFile(file));
                    file.renameTo(completedFile);
                }
            }

        }
        catch (IOException e ) {
            throw new IOException(e.getMessage());
        }
    }


    public boolean writeMessageToLogS3(MessageDTO messageDTO,String transformedPayload, int destinationId, int sourceId) {


        Span s3Span = TraceHelper.createSpan(tracing, messageDTO.getHeaders(), TracingConstant.SpanNames.DESTINATION_S3, messageDTO.getTopicName());

        try {
            String payload = StringEscapeUtils.unescapeJava(transformedPayload);
            writeMessageToFile(payload, destinationId, sourceId);
            return true;
        } catch (TransformerException te) {
            Span span = TraceHelper.createSpan(tracing, messageDTO.getHeaders(), TracingConstant.SpanNames.DESTINATION_S3, messageDTO.getTopicName());
            TransformerException transformerException = new TransformerException(
                    te.getMessage(),
                    StringUtils.isNotEmpty(te.getTopic()) ? te.getTopic() : messageDTO.getTopicName(),
                    StringUtils.isNotEmpty(te.getKafkaPayload()) ? te.getKafkaPayload() : messageDTO.getMessage(),
                    Objects.nonNull(te.getDropPayload()) && te.getDropPayload());
            transformerExceptionHandler.handle(transformerException, span);
        } catch (Exception e) {
            Span span = TraceHelper.createSpan(tracing, messageDTO.getHeaders(), TracingConstant.SpanNames.DESTINATION_S3, messageDTO.getTopicName());
            TransformerException transformerException = new TransformerException(e.getMessage(), messageDTO.getTopicName(), messageDTO.getMessage(), false);
            transformerExceptionHandler.handle(transformerException, span);
            LOGGER.error("Exception in processing", e);
        }
        finally {
            TraceHelper.closeSpan(messageDTO.getHeaders(),s3Span);
        }

        return false;

    }

}
