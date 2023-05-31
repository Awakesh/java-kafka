package co.lemnisk.transform.analyzepost.builder;

import co.lemnisk.common.constants.ExceptionMessage;
import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.common.avro.model.event.identify.web.IdentifyWeb;
import co.lemnisk.common.avro.model.event.page.Page;
import co.lemnisk.common.avro.model.event.track.web.TrackWeb;
import co.lemnisk.common.constants.EventType;
import co.lemnisk.transform.analyzepost.builder.v1.V1IdentifyWebBuilder;
import co.lemnisk.transform.analyzepost.builder.v1.V1PageBuilder;
import co.lemnisk.transform.analyzepost.builder.v1.V1TrackWebBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

public class V1DataBuilder {

    private final LinkedTreeMap<String, String> parsedData;
    @Getter
    private final String inputPayload;
    @Getter
    private final String eventType;

    @Autowired
    AutowireCapableBeanFactory autowireCapableBeanFactory;

    public V1DataBuilder(String jsonStr) {
        final Gson gson = new Gson();
        this.parsedData = (LinkedTreeMap<String, String>)gson.fromJson(jsonStr, Object.class);
        this.inputPayload = jsonStr;
        this.eventType = getInstanceType();
    }

    private String getInstanceType() {
        if (isPageEvent())
            return EventType.PAGE;
        else if (isTrackEvent())
            return EventType.TRACK;
        else if (isIdentifyEvent())
            return EventType.IDENTIFY;
        else
            throw new TransformerException(ExceptionMessage.INVALID_EVENT_TYPE);
    }

    private boolean isPageEvent() {
        return this.parsedData.containsKey(EventType.PAGE);
    }

    private boolean isTrackEvent() {
        return this.parsedData.containsKey(EventType.TRACK);
    }

    private boolean isIdentifyEvent() {
        return this.parsedData.containsKey(EventType.IDENTIFY);
    }

    public Page buildPageEventData() throws JsonProcessingException {
        V1PageBuilder v1PageBuilder = new V1PageBuilder(inputPayload);
        autowireCapableBeanFactory.autowireBean(v1PageBuilder);
        return v1PageBuilder
                .validateWithException()
                .build();
    }

    public IdentifyWeb buildIdentifyEventData() throws JsonProcessingException {
        V1IdentifyWebBuilder v1IdentifyWebBuilder = new V1IdentifyWebBuilder(inputPayload);
        autowireCapableBeanFactory.autowireBean(v1IdentifyWebBuilder);
        return v1IdentifyWebBuilder
                .validateWithException()
                .build();
    }

    public TrackWeb buildTrackEventData() throws JsonProcessingException {
        V1TrackWebBuilder v1TrackWebBuilder = new V1TrackWebBuilder(inputPayload);
        autowireCapableBeanFactory.autowireBean(v1TrackWebBuilder);
        return v1TrackWebBuilder
                .validateWithException()
                .build();
    }

}
