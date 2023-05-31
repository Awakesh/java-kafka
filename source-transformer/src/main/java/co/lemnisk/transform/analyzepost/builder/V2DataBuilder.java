package co.lemnisk.transform.analyzepost.builder;

import co.lemnisk.common.constants.Constants;
import co.lemnisk.common.metrics.EventMonitoring;
import co.lemnisk.common.avro.model.event.identify.app.IdentifyApp;
import co.lemnisk.common.avro.model.event.screen.Screen;
import co.lemnisk.common.avro.model.event.track.app.TrackApp;
import co.lemnisk.common.constants.EventType;
import co.lemnisk.transform.CampaignIdParser;
import co.lemnisk.transform.analyzepost.builder.v2.V2IdentifyAppBuilder;
import co.lemnisk.transform.analyzepost.builder.v2.V2ScreenBuilder;
import co.lemnisk.transform.analyzepost.builder.v2.V2TrackAppBuilder;
import co.lemnisk.transform.analyzepost.model.parser.v2.V2DataParser;
import co.lemnisk.validation.Validation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

public class V2DataBuilder {

    V2DataParser data;

    @Getter
    public String rawData;

    @Autowired
    AutowireCapableBeanFactory autowireCapableBeanFactory;

    public V2DataBuilder(String jsonStr) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        data = om.readValue(jsonStr, V2DataParser.class);
        this.rawData = jsonStr;
    }

    public boolean isTrackEvent() {
        return EventType.TRACK.equals(eventType());
    }

    public boolean isIdentifyEvent() {
        return EventType.IDENTIFY.equals(eventType());
    }

    public boolean isScreenEvent() {
        return EventType.SCREEN.equals(eventType());
    }

    private CharSequence eventType(){
        CharSequence input = (CharSequence) data.getProp().get("event_type");
        if(input == null || input.equals("")) {
            input = (CharSequence) data.getProp().get("eventCategory");
        }
        return input;
    }

    public void validateWithException() {
        String messageId = data.getContext().getMsgId();
        String ipAddress = data.getContext().getIp();
        String accountId = String.valueOf(CampaignIdParser.parse(data.getContext().getAccount_id()));
        String serverTs = data.getContext().getServer_ts();
        String srcId = data.getContext().getSrcid();
        Validation validationObj = new Validation();

        validationObj.validate(messageId, ipAddress, accountId, serverTs, srcId);
    }

    public IdentifyApp buildIdentifyApp() throws JsonProcessingException {
        V2IdentifyAppBuilder v2IdentifyAppBuilder = new V2IdentifyAppBuilder(rawData);
        autowireCapableBeanFactory.autowireBean(v2IdentifyAppBuilder);
        return v2IdentifyAppBuilder
                .getIdentifyData();
    }

    public TrackApp buildTrackApp() throws JsonProcessingException {
        V2TrackAppBuilder v2TrackAppBuilder = new V2TrackAppBuilder(rawData);
        autowireCapableBeanFactory.autowireBean(v2TrackAppBuilder);
        return v2TrackAppBuilder
                .getTrackData();
    }

    public Screen buildScreen() throws JsonProcessingException {
        V2ScreenBuilder v2ScreenBuilder = new V2ScreenBuilder(rawData);
        autowireCapableBeanFactory.autowireBean(v2ScreenBuilder);
        return v2ScreenBuilder
                .getScreenData();
    }

    public void addMetrics(V2DataBuilder v2DataBuilderObj, EventMonitoring eventMonitoring){
        eventMonitoring.addCdpMetric(String.valueOf(CampaignIdParser.parse(v2DataBuilderObj.data.getContext().getAccount_id())),
                String.valueOf(v2DataBuilderObj.data.getProp().get("srcid")),
                v2DataBuilderObj.data.getEventName(),
                v2DataBuilderObj.data.eventType(),
                Constants.ModuleNames.SOURCE_TRANSFORMER);

        eventMonitoring.addCdpEventPropertiesMetric(String.valueOf(CampaignIdParser.parse(v2DataBuilderObj.data.getContext().getAccount_id())),
                String.valueOf(v2DataBuilderObj.data.getProp().get("srcid")),
                v2DataBuilderObj.data.getEventName(),
                v2DataBuilderObj.data.getProp(),
                v2DataBuilderObj.data.eventType());
    }

}
