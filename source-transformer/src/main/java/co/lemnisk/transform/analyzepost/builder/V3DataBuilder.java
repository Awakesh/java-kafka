package co.lemnisk.transform.analyzepost.builder;

import co.lemnisk.common.avro.model.event.identify.app.IdentifyApp;
import co.lemnisk.common.avro.model.event.identify.web.IdentifyWeb;
import co.lemnisk.common.avro.model.event.page.Page;
import co.lemnisk.common.avro.model.event.screen.Screen;
import co.lemnisk.common.avro.model.event.track.app.TrackApp;
import co.lemnisk.common.avro.model.event.track.web.TrackWeb;
import co.lemnisk.common.constants.EventType;
import co.lemnisk.common.constants.SourceTransformerConstant;
import co.lemnisk.transform.analyzepost.builder.v3.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class V3DataBuilder {

    @Autowired
    AutowireCapableBeanFactory autowireCapableBeanFactory;

    @Getter
    public final String rawData;

    public V3DataBuilder(String jsonStr) {
        this.rawData = jsonStr;
    }

    public IdentifyWeb buildIdentifyWeb() throws JsonProcessingException {
        V3IdentifyWebBuilder v3IdentifyWebBuilder = new V3IdentifyWebBuilder();
        autowireCapableBeanFactory.autowireBean(v3IdentifyWebBuilder);
        return v3IdentifyWebBuilder
                .setRawData(rawData)
                .validateWithException()
                .build();
    }

    public IdentifyApp buildIdentifyApp() throws JsonProcessingException {
        V3IdentifyAppBuilder v3IdentifyAppBuilder = new V3IdentifyAppBuilder();
        autowireCapableBeanFactory.autowireBean(v3IdentifyAppBuilder);
        return v3IdentifyAppBuilder
                .setRawData(rawData)
                .validateWithException()
                .build();
    }

    public TrackWeb buildTrackWeb() throws JsonProcessingException {
        V3TrackWebBuilder v3TrackWebBuilder = new V3TrackWebBuilder(rawData);
        autowireCapableBeanFactory.autowireBean(v3TrackWebBuilder);
        return v3TrackWebBuilder
                .validateWithException()
                .build();
    }

    public TrackApp buildTrackApp() throws JsonProcessingException {
        V3TrackAppBuilder v3TrackAppBuilder = new V3TrackAppBuilder(rawData);
        autowireCapableBeanFactory.autowireBean(v3TrackAppBuilder);
        return v3TrackAppBuilder
                .validateWithException()
                .build();
    }

    public Page buildPage() throws JsonProcessingException {
        V3PageBuilder v3PageBuilder = new V3PageBuilder();
        autowireCapableBeanFactory.autowireBean(v3PageBuilder);
        return v3PageBuilder
                .setRawData(rawData)
                .validateWithException()
                .build();
    }

    public Screen buildScreen() throws JsonProcessingException {
        V3ScreenBuilder v3ScreenBuilder = new V3ScreenBuilder();
        autowireCapableBeanFactory.autowireBean(v3ScreenBuilder);
        return v3ScreenBuilder
                .setRawData(rawData)
                .validateWithException()
                .build();
    }

    public boolean isPageEvent() {
        return validateEventType(EventType.PAGE);
    }

    public boolean isScreenEvent() {
        return validateEventType(EventType.SCREEN);
    }

    public boolean isTrackAppEvent() {
        return validateEventType(EventType.TRACK) && isMobileAppEvent();
    }

    public boolean isTrackWebEvent() {
        return validateEventType(EventType.TRACK) && !isMobileAppEvent();
    }

    public boolean isIdentifyAppEvent() {
        return validateEventType(EventType.IDENTIFY) && isMobileAppEvent();
    }

    public boolean isIdentifyWebEvent() {
        return validateEventType(EventType.IDENTIFY) && !isMobileAppEvent();
    }

    private boolean isMobileAppEvent() {

        Predicate<String> androidMatchesRegex = Pattern.compile(SourceTransformerConstant.ANDROID_REGEX).asMatchPredicate();
        Predicate<String> iosMatchesRegex = Pattern.compile(SourceTransformerConstant.IOS_REGEX).asMatchPredicate();

        return androidMatchesRegex.test(rawData) || iosMatchesRegex.test(rawData);
    }

    private boolean validateEventType(String eventType) {

        String regex = String.format(".*\"type\":\\s*\"%s\".*", eventType);
        Predicate<String> matchesRegex = Pattern.compile(regex).asMatchPredicate();
        return matchesRegex.test(rawData);
    }
}
