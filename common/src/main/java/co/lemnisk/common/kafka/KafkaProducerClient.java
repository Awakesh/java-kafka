package co.lemnisk.common.kafka;

import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.common.exception.TransformerExceptionHandler;
import co.lemnisk.common.kafka.configuration.ProducerConfiguration;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Properties;

@NoArgsConstructor
@Component
public class KafkaProducerClient {
    static final Logger logger = LoggerFactory.getLogger(KafkaProducerClient.class);
    private KafkaProducer<String, String> producer;

    @Lazy
    @Autowired
    TransformerExceptionHandler transformerExceptionHandler;

    @PostConstruct
    void initProducer() {
        Properties properties = ProducerConfiguration.config();
        this.producer = new org.apache.kafka.clients.producer.KafkaProducer<>(properties);
    }

    public void sendMessageToDLT(String topicName, String inputRecord, Exception exception){

        String dltTopic = null;
        try {
            dltTopic = dltTopicName(topicName);
            ProducerRecord<String, String> producerRecord = getProducerRecord(topicName, dltTopic, inputRecord, exception);

            logger.info("Sending message to Kafka DLT topic: {}", dltTopic);
            producer.send(producerRecord);
        }
        catch (Exception ex) {
            TransformerException transformerException = new TransformerException("Error while sending message to DLT topic: " + dltTopic, topicName, inputRecord, true);
            transformerExceptionHandler.handle(transformerException);
        }
    }

    private ProducerRecord<String, String> getProducerRecord(String originalTopicName,
                                                             String dltTopic,
                                                             String inputRecord,
                                                             Exception exception) {

        ProducerRecord<String, String> prodRecord= new ProducerRecord<>(dltTopic, inputRecord);
        ArrayList<Header> headers = getDLTHeadersList(originalTopicName, exception);

        for (Header header : headers) {
            prodRecord.headers().add(header);
        }

        return prodRecord;
    }

    private ArrayList<Header> getDLTHeadersList(String originalTopic, Exception ex) {
        ArrayList<Header> headerList = new ArrayList<>();
        headerList.add(
                new RecordHeader(KafkaHeaders.DLT_ORIGINAL_TOPIC,
                        originalTopic.getBytes(StandardCharsets.UTF_8)));

        headerList.add(
                new RecordHeader(KafkaHeaders.DLT_EXCEPTION_MESSAGE,
                        replaceNullToBlank(ex.getMessage()).getBytes(StandardCharsets.UTF_8)));

        headerList.add(
                new RecordHeader(KafkaHeaders.DLT_EXCEPTION_STACKTRACE,
                        getStackTraceAsString(ex).getBytes(StandardCharsets.UTF_8)));

        return headerList;
    }

    private static String replaceNullToBlank(String input) {
        return input == null ? "" : input;
    }

    private String getStackTraceAsString(Throwable cause) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter, true);
        cause.printStackTrace(printWriter);
        return stringWriter.getBuffer().toString();
    }

    private String dltTopicName(String topic) {
        return topic + ".DLT";
    }
}
