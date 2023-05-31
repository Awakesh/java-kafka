package co.lemnisk;

import co.lemnisk.common.avro.model.event.dmpnba.DmpNba;
import co.lemnisk.common.avro.model.event.identify.app.IdentifyApp;
import co.lemnisk.common.avro.model.event.identify.web.IdentifyWeb;
import co.lemnisk.common.avro.model.event.page.Page;
import co.lemnisk.common.avro.model.event.screen.Screen;
import co.lemnisk.common.avro.model.event.track.app.TrackApp;
import co.lemnisk.common.avro.model.event.track.web.TrackWeb;
import co.lemnisk.common.constants.Constants;
import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.common.metrics.EventMonitoring;
import co.lemnisk.common.service.TransformerFunctionService;
import co.lemnisk.common.tracing.TraceHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentelemetry.api.trace.Span;
import org.apache.avro.specific.SpecificRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.script.ScriptException;
import java.util.Map;

@Component
public class DataTransformer {

    @Autowired
    TransformerFunctionService transformerFunctionService;

    @Autowired
    JSTransformer jsTransformer;

    @Autowired
    EventMonitoring eventMonitoring;

    ObjectMapper mapper = new ObjectMapper();

    // TODO: Add Jaeger
    public Object castAndTransformEventData(SpecificRecord event, Span span) throws TransformerException, JsonProcessingException, ScriptException, NoSuchMethodException {
        if (event instanceof TrackWeb)
            return transformEventData((TrackWeb) event, span);
        else if (event instanceof TrackApp)
            return transformEventData((TrackApp) event, span);
        else if (event instanceof Page)
            return transformEventData((Page) event, span);
        else if (event instanceof Screen)
            return transformEventData((Screen) event, span);
        else if (event instanceof IdentifyWeb)
            return transformEventData((IdentifyWeb) event, span);
        else if (event instanceof IdentifyApp)
            return transformEventData((IdentifyApp) event, span);
        else if (event instanceof DmpNba)
            return transformEventData((DmpNba) event, span);
        else throw new TransformerException("Unknown type of event data");
    }

    private Object transformEventData(TrackWeb event, Span span) throws ScriptException, NoSuchMethodException {
        String transformerFunction;
        String eventName;

        int destinationInstanceId = Integer.parseInt(event.getDestinationInstanceId().toString());
        String eventType = event.getType().toString();
        String campaignIdData = String.valueOf(event.getContext().getAccountId());
        String messageId = event.getMessageId().toString();
        String srcId = String.valueOf(event.getContext().getSrcId());
        Map<CharSequence,Object> propertiesData = event.getProperties();

        TraceHelper.addSpanAttributes(span, eventType, messageId, campaignIdData, destinationInstanceId);
        
        if (Boolean.TRUE.equals(event.getIsStandardEvent())) {
            eventName = event.getEvent().toString();
            transformerFunction = transformerFunctionService.getStandardTransformerFunction(eventName, destinationInstanceId, eventType);
        }
        else if (StringUtils.isNotEmpty(event.getLemEvent().toString())) {
             eventName = event.getLemEvent().toString();
             transformerFunction = transformerFunctionService.getStandardTransformerFunction(eventName, destinationInstanceId, eventType);
        }
        else {
            eventName = event.getEvent().toString();
            int campaignId = Integer.parseInt(event.getContext().getAccountId().toString());
            transformerFunction = transformerFunctionService.getCustomTransformerFunction(eventName, campaignId, destinationInstanceId, eventType);
        }
        String stringifiedEvent = event.toString();
        addMetrics(eventMonitoring, campaignIdData,srcId,eventName,eventType,propertiesData);

        return jsTransformer.transformEvent(stringifiedEvent, transformerFunction);
    }

    private Object transformEventData(TrackApp event, Span span) throws JsonProcessingException, ScriptException, NoSuchMethodException {
        String transformerFunction;
        String eventName;
        int destinationInstanceId = Integer.parseInt(event.getDestinationInstanceId().toString());
        String eventType = event.getType().toString();
        String messageId = event.getMessageId().toString();
        String campaignIdData = String.valueOf(event.getContext().getAccountId());
        String srcId = String.valueOf(event.getContext().getSrcId());
        Map<CharSequence,Object> propertiesData = event.getProperties();

        TraceHelper.addSpanAttributes(span, eventType, messageId, campaignIdData, destinationInstanceId);

        if (Boolean.TRUE.equals(event.getIsStandardEvent())) {
            eventName = event.getEvent().toString();
            transformerFunction = transformerFunctionService.getStandardTransformerFunction(eventName, destinationInstanceId, eventType);
        }
        else if (StringUtils.isNotEmpty(event.getLemEvent().toString())) {
             eventName = event.getLemEvent().toString();
             transformerFunction = transformerFunctionService.getStandardTransformerFunction(eventName, destinationInstanceId, eventType);
        }
        else {
            eventName = event.getEvent().toString();
            int campaignId = Integer.parseInt(event.getContext().getAccountId().toString());
            transformerFunction = transformerFunctionService.getCustomTransformerFunction(eventName, campaignId, destinationInstanceId, eventType);
        }

        String stringifiedEvent = event.toString();
        addMetrics(eventMonitoring, campaignIdData,srcId,eventName,eventType,propertiesData);

        return jsTransformer.transformEvent(stringifiedEvent, transformerFunction);
    }

    private Object transformEventData(DmpNba event, Span span) throws ScriptException, NoSuchMethodException {
        String transformerFunction;
        String eventName;

        int destinationInstanceId = Integer.parseInt(event.getDestinationInstanceId().toString());
        String eventType = event.getType().toString();
        String messageId = event.getMessageId().toString();
        String campaignIdData = String.valueOf(event.getContext().getAccountId());
        String srcId = String.valueOf(event.getContext().getSrcId());
        Map<CharSequence,Object> propertiesData = event.getProperties();

        TraceHelper.addSpanAttributes(span, eventType, messageId, campaignIdData, destinationInstanceId);

        if (Boolean.TRUE.equals(event.getIsStandardEvent())) {
            if (Constants.EventTypes.IDENTIFY.equals(eventType))
                eventName = eventType;
            else
                eventName = event.getEvent().toString();

            transformerFunction = transformerFunctionService.getStandardTransformerFunction(eventName, destinationInstanceId, eventType);
        }
        else if (StringUtils.isNotEmpty(event.getLemEvent().toString())) {
            eventName = event.getLemEvent().toString();
            transformerFunction = transformerFunctionService.getStandardTransformerFunction(eventName, destinationInstanceId, eventType);
        }
        else {
            eventName = event.getEvent().toString();
            int campaignId = Integer.parseInt(event.getContext().getAccountId().toString());
            transformerFunction = transformerFunctionService.getCustomTransformerFunction(eventName, campaignId, destinationInstanceId, eventType);
        }
        String stringifiedEvent = event.toString();
        addMetrics(eventMonitoring, campaignIdData,srcId,eventName,eventType,propertiesData);

        return jsTransformer.transformEvent(stringifiedEvent, transformerFunction);
    }

    private Object transformEventData(Page event, Span span) throws JsonProcessingException, ScriptException, NoSuchMethodException {
        int destinationInstanceId = Integer.parseInt(event.getDestinationInstanceId().toString());
        String eventType = event.getType().toString();
        String messageId = event.getMessageId().toString();
        String campaignIdData = String.valueOf(event.getContext().getAccountId());
        String srcId = String.valueOf(event.getContext().getSrcId());
        Map<CharSequence,Object> propertiesData = event.getProperties();
        String eventName = eventType;
        TraceHelper.addSpanAttributes(span, eventType, messageId, campaignIdData, destinationInstanceId);
        String transformerFunction = transformerFunctionService.getStandardTransformerFunction(eventName, destinationInstanceId, eventType);
        String stringifiedEvent = event.toString();
        addMetrics(eventMonitoring, campaignIdData,srcId,eventName,eventType,propertiesData);

        return jsTransformer.transformEvent(stringifiedEvent, transformerFunction);
    }

    private Object transformEventData(Screen event, Span span) throws JsonProcessingException, ScriptException, NoSuchMethodException {
        int destinationInstanceId = Integer.parseInt(event.getDestinationInstanceId().toString());
        String eventType = event.getType().toString();
        String messageId = event.getMessageId().toString();
        String campaignIdData = String.valueOf(event.getContext().getAccountId());
        String srcId = String.valueOf(event.getContext().getSrcId());
        Map<CharSequence,Object> propertiesData = event.getProperties();
        String eventName = eventType;
        TraceHelper.addSpanAttributes(span, eventType, messageId, campaignIdData, destinationInstanceId);
        String transformerFunction = transformerFunctionService.getStandardTransformerFunction(eventName, destinationInstanceId, eventType);
        String stringifiedEvent = event.toString();
        addMetrics(eventMonitoring, campaignIdData,srcId,eventName,eventType,propertiesData);

        return jsTransformer.transformEvent(stringifiedEvent, transformerFunction);
    }

    private Object transformEventData(IdentifyWeb event, Span span) throws JsonProcessingException, ScriptException, NoSuchMethodException {
        int destinationInstanceId = Integer.parseInt(event.getDestinationInstanceId().toString());
        String eventType = event.getType().toString();
        String messageId = event.getMessageId().toString();
        String campaignIdData = String.valueOf(event.getContext().getAccountId());
        String srcId = String.valueOf(event.getContext().getSrcId());
        Map<CharSequence,Object> propertiesData = event.getCustomerProperties();
        String eventName = eventType;
        TraceHelper.addSpanAttributes(span, eventType, messageId, campaignIdData, destinationInstanceId);
        String transformerFunction = transformerFunctionService.getStandardTransformerFunction(eventName, destinationInstanceId, eventType);
        String stringifiedEvent = event.toString();
        addMetrics(eventMonitoring, campaignIdData,srcId,eventName,eventType,propertiesData);

        return jsTransformer.transformEvent(stringifiedEvent, transformerFunction);
    }

    private Object transformEventData(IdentifyApp event, Span span) throws JsonProcessingException, ScriptException, NoSuchMethodException {
        int destinationInstanceId = Integer.parseInt(event.getDestinationInstanceId().toString());
        String eventType = event.getType().toString();
        String messageId = event.getMessageId().toString();
        String campaignIdData = String.valueOf(event.getContext().getAccountId());
        String srcId = String.valueOf(event.getContext().getSrcId());
        Map<CharSequence,Object> propertiesData = event.getCustomerProperties();
        String eventName = eventType;
        TraceHelper.addSpanAttributes(span, eventType, messageId, campaignIdData, destinationInstanceId);
        String transformerFunction = transformerFunctionService.getStandardTransformerFunction(eventName, destinationInstanceId, eventType);
        String stringifiedEvent = event.toString();
        addMetrics(eventMonitoring, campaignIdData,srcId,eventName,eventType,propertiesData);

        return jsTransformer.transformEvent(stringifiedEvent, transformerFunction);
    }

    public void addMetrics(EventMonitoring eventMonitoring, String campaignId, String srcId, String eventName, String eventType, Map<CharSequence,Object> propertiesData){
        eventMonitoring.addCdpMetric(campaignId, srcId, eventName, eventType, Constants.ModuleNames.DESTINATION_TRANSFORMER);

        eventMonitoring.addCdpEventPropertiesMetric(campaignId, srcId, eventName, propertiesData, eventType);
    }
}
