package co.lemnisk.transform.dmpnbaapi.serde;

import co.lemnisk.common.tracing.TraceHelper;
import co.lemnisk.common.tracing.Tracing;
import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.common.exception.TransformerExceptionHandler;
import co.lemnisk.common.kafka.KafkaProducerClient;
import co.lemnisk.loggers.DmpNbaEventLogger;
import co.lemnisk.common.logging.EventLogger;
import co.lemnisk.transform.dmpnbaapi.model.DmpNbaApiInputPayload;
import io.opentelemetry.api.trace.Span;

import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class DmpNbaApiDeserializer implements Deserializer<DmpNbaApiInputPayload> {

    @Autowired
    KafkaProducerClient kafkaProducerClient;

    @Autowired
    TransformerExceptionHandler transformerExceptionHandler;

    @Autowired
    Tracing tracing;

    private Span span;

    @Override
    public DmpNbaApiInputPayload deserialize(String topic, byte[] byteData) {
        if(byteData == null)
            return null;

        String deserializedMessage = new String(byteData, StandardCharsets.UTF_8);

        DmpNbaEventLogger.getInstance().log(topic, EventLogger.MESSAGE_FLOW.INCOMING, deserializedMessage);

        try {
            DmpNbaApiInputPayload dmpNbaApiInputPayload = new DmpNbaApiInputPayload();
            dmpNbaApiInputPayload.process(deserializedMessage);

            return dmpNbaApiInputPayload;
        }
        catch (TransformerException transformerException) {
            transformerException.setDropPayload(true);
            transformerExceptionHandler.handle(transformerException, span);

            return null;
        }
        catch(Exception e) {
            TransformerException transformerException = new TransformerException(e.getMessage(), topic, deserializedMessage, false);
            transformerExceptionHandler.handle(transformerException, span);
            return null;
        }

    }

    @Override
    public DmpNbaApiInputPayload deserialize(String topic, Headers headers, byte[] data) {
        this.span = TraceHelper.createSpan(tracing, headers, "DmpNbaApiDeserializer", topic);

        try {
            return this.deserialize(topic, data);
        }
        catch (Exception e) {
            TraceHelper.handleException(span, e);
            return null;
        }
        finally {
            TraceHelper.closeSpan(headers, span);
        }
    }
}
