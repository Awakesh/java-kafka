package co.lemnisk;

import co.lemnisk.common.avro.model.event.dmpnba.DmpNba;
import co.lemnisk.common.avro.model.event.identify.app.IdentifyApp;
import co.lemnisk.common.constants.Constants;
import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.common.avro.model.event.identify.web.IdentifyWeb;
import co.lemnisk.common.avro.model.event.page.Page;
import co.lemnisk.common.avro.model.event.screen.Screen;
import co.lemnisk.common.avro.model.event.track.app.TrackApp;
import co.lemnisk.common.avro.model.event.track.web.TrackWeb;
import co.lemnisk.common.metrics.MonitoringConstant;
import co.lemnisk.common.metrics.EventMonitoring;
import co.lemnisk.common.service.CDPDestinationService;
import co.lemnisk.common.service.DestinationFilteringService;
import co.lemnisk.common.tracing.TraceHelper;
import io.opentelemetry.api.trace.Span;
import org.apache.avro.specific.SpecificRecord;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.streams.KeyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.*;

@Component
public class DataMultiplier {

    @Autowired
    EventMonitoring eventMonitoring;

    @Autowired
    DestinationFilteringService destinationFilteringService;

    @Autowired
    CDPDestinationService cdpDestinationService;

    public List<KeyValue<String, SpecificRecord>> castAndCloneEventData(String key, SpecificRecord value, Span span) throws TransformerException {
        if (value instanceof TrackWeb)
            return createCopiesForEachDestInstance(key, (TrackWeb) value, span);
        else if (value instanceof TrackApp)
            return createCopiesForEachDestInstance(key, (TrackApp) value, span);
        else if (value instanceof Page)
            return createCopiesForEachDestInstance(key, (Page) value, span);
        else if (value instanceof Screen)
            return createCopiesForEachDestInstance(key, (Screen) value, span);
        else if (value instanceof IdentifyWeb)
            return createCopiesForEachDestInstance(key, (IdentifyWeb) value, span);
        else if (value instanceof IdentifyApp)
            return createCopiesForEachDestInstance(key, (IdentifyApp) value, span);
        else if (value instanceof DmpNba)
            return createCopiesForEachDestInstance(key, (DmpNba) value, span);
        else throw new TransformerException("Unknown type of event data");
    }

    public List<KeyValue<String, SpecificRecord>> createCopiesForEachDestInstance(String key, TrackWeb eventData, Span span) {

        int campaignId = Integer.parseInt(eventData.getContext().getAccountId().toString());
        int srcId = Integer.parseInt(eventData.getContext().getSrcId().toString());
        Map<CharSequence, Object> propertiesData = eventData.getProperties();
        String messageId = eventData.getMessageId().toString();
        String eventType = eventData.getType().toString();
        String eventName = eventData.getEvent().toString();
        TraceHelper.addSpanAttributes(span, eventType, messageId, String.valueOf(campaignId));
        addMetrics(eventMonitoring,campaignId,srcId,eventName,eventType,propertiesData);

        Set<Integer> destinationInstanceIds = destinationFilteringService.getDestinationInstanceIds(campaignId, eventName, eventType, srcId, eventData.getIsStandardEvent());

        List<KeyValue<String, SpecificRecord>> dataList = new LinkedList<>();

        for (int destinationInstanceId : destinationInstanceIds) {

            Integer inputLivenessId = cdpDestinationService.getLivenessDuration(destinationInstanceId);
            if (inputLivenessId != -1) {
                String s2 = String.valueOf(eventData.getServerTs());
                long inputServerTs = Long.parseLong(s2);
                Instant instant = Instant.ofEpochMilli(inputServerTs);
                Instant instant1 = instant.plusSeconds(inputLivenessId * 60);
                if (instant1.isAfter(Instant.now())) {
                    eventMonitoring.addProcessingErrorMetrics(String.valueOf(campaignId), MonitoringConstant.LIVENESS_ERROR, Constants.ModuleNames.ROUTER);
                    throw new TransformerException("Invalid Liveness duration");
                }
            }

            TrackWeb clonedObject = SerializationUtils.clone(eventData);
            clonedObject.setDestinationInstanceId(Integer.toString(destinationInstanceId));
            dataList.add(KeyValue.pair(key, clonedObject));
        }

        return dataList;
    }

    public List<KeyValue<String, SpecificRecord>> createCopiesForEachDestInstance(String key, TrackApp eventData, Span span) {

        int campaignId = Integer.parseInt(eventData.getContext().getAccountId().toString());
        int srcId = Integer.parseInt(eventData.getContext().getSrcId().toString());
        Map<CharSequence, Object> propertiesData = eventData.getProperties();
        String messageId = eventData.getMessageId().toString();
        String eventType = eventData.getType().toString();
        String eventName = eventData.getEvent().toString();
        TraceHelper.addSpanAttributes(span, eventType, messageId, String.valueOf(campaignId));
        addMetrics(eventMonitoring,campaignId,srcId,eventName,eventType,propertiesData);

        Set<Integer> destinationInstanceIds = destinationFilteringService.getDestinationInstanceIds(campaignId, eventName, eventType, srcId, eventData.getIsStandardEvent());

        List<KeyValue<String, SpecificRecord>> dataList = new LinkedList<>();

        for (int destinationInstanceId : destinationInstanceIds) {

            Integer inputLivenessId = cdpDestinationService.getLivenessDuration(destinationInstanceId);
            if (inputLivenessId != -1) {
                String s2 = String.valueOf(eventData.getServerTs());
                long inputServerTs = Long.parseLong(s2);
                Instant instant = Instant.ofEpochMilli(inputServerTs);
                Instant instant1 = instant.plusSeconds(inputLivenessId * 60);
                if (instant1.isAfter(Instant.now())) {
                    eventMonitoring.addProcessingErrorMetrics(String.valueOf(campaignId), MonitoringConstant.LIVENESS_ERROR, Constants.ModuleNames.ROUTER);
                    throw new TransformerException("Invalid Liveness duration");
                }
            }

            TrackApp clonedObject = SerializationUtils.clone(eventData);
            clonedObject.setDestinationInstanceId(Integer.toString(destinationInstanceId));
            dataList.add(KeyValue.pair(key, clonedObject));
        }

        return dataList;
    }

    public List<KeyValue<String, SpecificRecord>> createCopiesForEachDestInstance(String key, Page eventData, Span span) {

        int campaignId = Integer.parseInt(eventData.getContext().getAccountId().toString());
        int srcId = Integer.parseInt(eventData.getContext().getSrcId().toString());
        Map<CharSequence, Object> propertiesData = eventData.getProperties();
        String messageId = eventData.getMessageId().toString();
        String eventType = eventData.getType().toString();
        String eventName = eventData.getType().toString();
        TraceHelper.addSpanAttributes(span, eventType, messageId, String.valueOf(campaignId));
        addMetrics(eventMonitoring,campaignId,srcId,eventName,eventType,propertiesData);

        Set<Integer> destinationInstanceIds = destinationFilteringService.getDestinationInstanceIds(campaignId, eventName, eventType, srcId, eventData.getIsStandardEvent());

        List<KeyValue<String, SpecificRecord>> dataList = new LinkedList<>();

        for (int destinationInstanceId : destinationInstanceIds) {

            Integer inputLivenessId = cdpDestinationService.getLivenessDuration(destinationInstanceId);
            if (inputLivenessId != -1) {
                String s2 = String.valueOf(eventData.getServerTs());
                long inputServerTs = Long.parseLong(s2);
                Instant instant = Instant.ofEpochMilli(inputServerTs);
                Instant instant1 = instant.plusSeconds(inputLivenessId * 60);
                if (instant1.isAfter(Instant.now())) {
                    eventMonitoring.addProcessingErrorMetrics(String.valueOf(campaignId), MonitoringConstant.LIVENESS_ERROR, Constants.ModuleNames.ROUTER);
                    throw new TransformerException("Invalid Liveness duration");
                }
            }

            Page clonedObject = SerializationUtils.clone(eventData);
            clonedObject.setDestinationInstanceId(Integer.toString(destinationInstanceId));
            dataList.add(KeyValue.pair(key, clonedObject));
        }

        return dataList;
    }

    public List<KeyValue<String, SpecificRecord>> createCopiesForEachDestInstance(String key, Screen eventData, Span span) {

        int campaignId = Integer.parseInt(eventData.getContext().getAccountId().toString());
        int srcId = Integer.parseInt(eventData.getContext().getSrcId().toString());
        Map<CharSequence, Object> propertiesData = eventData.getProperties();
        String messageId = eventData.getMessageId().toString();
        String eventType = eventData.getType().toString();
        String eventName = eventData.getType().toString();
        TraceHelper.addSpanAttributes(span, eventType, messageId, String.valueOf(campaignId));
        addMetrics(eventMonitoring,campaignId,srcId,eventName,eventType,propertiesData);

        Set<Integer> destinationInstanceIds = destinationFilteringService.getDestinationInstanceIds(campaignId, eventName, eventType, srcId, eventData.getIsStandardEvent());

        List<KeyValue<String, SpecificRecord>> dataList = new LinkedList<>();

        for (int destinationInstanceId : destinationInstanceIds) {

            Integer inputLivenessId = cdpDestinationService.getLivenessDuration(destinationInstanceId);
            if (inputLivenessId != -1) {
                String s2 = String.valueOf(eventData.getServerTs());
                long inputServerTs = Long.parseLong(s2);
                Instant instant = Instant.ofEpochMilli(inputServerTs);
                Instant instant1 = instant.plusSeconds(inputLivenessId * 60);
                if (instant1.isAfter(Instant.now())) {
                    eventMonitoring.addProcessingErrorMetrics(String.valueOf(campaignId), MonitoringConstant.LIVENESS_ERROR, Constants.ModuleNames.ROUTER);
                    throw new TransformerException("Invalid Liveness duration");
                }
            }

            Screen clonedObject = SerializationUtils.clone(eventData);
            clonedObject.setDestinationInstanceId(Integer.toString(destinationInstanceId));
            dataList.add(KeyValue.pair(key, clonedObject));
        }

        return dataList;
    }

    public List<KeyValue<String, SpecificRecord>> createCopiesForEachDestInstance(String key, IdentifyWeb eventData, Span span) {

        int campaignId = Integer.parseInt(eventData.getContext().getAccountId().toString());
        int srcId = Integer.parseInt(eventData.getContext().getSrcId().toString());
        Map<CharSequence, Object> customerPropertiesData = eventData.getCustomerProperties();
        String messageId = eventData.getMessageId().toString();
        String eventType = eventData.getType().toString();
        String eventName = eventData.getType().toString();
        TraceHelper.addSpanAttributes(span, eventType, messageId, String.valueOf(campaignId));
        addMetrics(eventMonitoring,campaignId,srcId,eventName,eventType,customerPropertiesData);

        Set<Integer> destinationInstanceIds = destinationFilteringService.getDestinationInstanceIds(campaignId, eventName, eventType, srcId, eventData.getIsStandardEvent());

        List<KeyValue<String, SpecificRecord>> dataList = new LinkedList<>();

        for (int destinationInstanceId : destinationInstanceIds) {

            Integer inputLivenessId = cdpDestinationService.getLivenessDuration(destinationInstanceId);
            if (inputLivenessId != -1) {
                String s2 = String.valueOf(eventData.getServerTs());
                long inputServerTs = Long.parseLong(s2);
                Instant instant = Instant.ofEpochMilli(inputServerTs);
                Instant instant1 = instant.plusSeconds(inputLivenessId * 60);
                if (instant1.isAfter(Instant.now())) {
                    eventMonitoring.addProcessingErrorMetrics(String.valueOf(campaignId), MonitoringConstant.LIVENESS_ERROR, Constants.ModuleNames.ROUTER);
                    throw new TransformerException("Invalid Liveness duration");
                }
            }

            IdentifyWeb clonedObject = SerializationUtils.clone(eventData);
            clonedObject.setDestinationInstanceId(Integer.toString(destinationInstanceId));
            dataList.add(KeyValue.pair(key, clonedObject));
        }

        return dataList;
    }

    public List<KeyValue<String, SpecificRecord>> createCopiesForEachDestInstance(String key, IdentifyApp eventData, Span span) {

        int campaignId = Integer.parseInt(eventData.getContext().getAccountId().toString());
        int srcId = Integer.parseInt(eventData.getContext().getSrcId().toString());
        Map<CharSequence, Object> customerPropertiesData = eventData.getCustomerProperties();
        String messageId = eventData.getMessageId().toString();
        String eventType = eventData.getType().toString();
        String eventName = eventData.getType().toString();
        TraceHelper.addSpanAttributes(span, eventType, messageId, String.valueOf(campaignId));
        addMetrics(eventMonitoring,campaignId,srcId,eventName,eventType,customerPropertiesData);

        Set<Integer> destinationInstanceIds = destinationFilteringService.getDestinationInstanceIds(campaignId, eventName, eventType, srcId, eventData.getIsStandardEvent());

        List<KeyValue<String, SpecificRecord>> dataList = new LinkedList<>();

        for (int destinationInstanceId : destinationInstanceIds) {

            Integer inputLivenessId = cdpDestinationService.getLivenessDuration(destinationInstanceId);
            if (inputLivenessId != -1) {
                String s2 = String.valueOf(eventData.getServerTs());
                long inputServerTs = Long.parseLong(s2);
                Instant instant = Instant.ofEpochMilli(inputServerTs);
                Instant instant1 = instant.plusSeconds(inputLivenessId * 60);
                if (instant1.isAfter(Instant.now())) {
                    eventMonitoring.addProcessingErrorMetrics(String.valueOf(campaignId), MonitoringConstant.LIVENESS_ERROR, Constants.ModuleNames.ROUTER);
                    throw new TransformerException("Invalid Liveness duration");
                }
            }

            IdentifyApp clonedObject = SerializationUtils.clone(eventData);
            clonedObject.setDestinationInstanceId(Integer.toString(destinationInstanceId));
            dataList.add(KeyValue.pair(key, clonedObject));
        }

        return dataList;
    }


    private List<KeyValue<String, SpecificRecord>> createCopiesForEachDestInstance(String key, DmpNba eventData, Span span) {
        int campaignId = Integer.parseInt(eventData.getContext().getAccountId().toString());
        int srcId = Integer.parseInt(eventData.getContext().getSrcId().toString());
        Map<CharSequence, Object> propertiesData = eventData.getProperties();
        String messageId = eventData.getMessageId().toString();
        String eventType = eventData.getType().toString();
        String eventName = eventData.getEvent().toString();
        TraceHelper.addSpanAttributes(span, eventType, messageId, String.valueOf(campaignId));
        addMetrics(eventMonitoring,campaignId,srcId,eventName,eventType,propertiesData);

        Set<Integer> destinationInstanceIds = destinationFilteringService.getDestinationInstanceIds(campaignId, eventName, eventType, srcId, eventData.getIsStandardEvent());

        List<KeyValue<String, SpecificRecord>> dataList = new LinkedList<>();

        for (int destinationInstanceId : destinationInstanceIds) {

            DmpNba clonedObject = SerializationUtils.clone(eventData);
            clonedObject.setDestinationInstanceId(Integer.toString(destinationInstanceId));
            dataList.add(KeyValue.pair(key, clonedObject));
        }

        return dataList;
    }

    public void addMetrics(EventMonitoring eventMonitoring, Integer campaignId, Integer srcId, String eventName, String eventType, Map<CharSequence,Object> propertiesData){
        eventMonitoring.addCdpMetric(String.valueOf(campaignId), String.valueOf(srcId), eventName, eventType, Constants.ModuleNames.ROUTER);

        eventMonitoring.addCdpEventPropertiesMetric(String.valueOf(campaignId),String.valueOf(srcId),eventName,propertiesData,eventType);
    }
}
