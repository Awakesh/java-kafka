package co.lemnisk.transform.analyzepost.builder.v2;

import co.lemnisk.common.Util;
import co.lemnisk.common.avro.model.event.track.app.TrackApp;
import co.lemnisk.common.model.CDPCustomEventsDictionary;
import co.lemnisk.common.model.CDPSourceInstance;
import co.lemnisk.common.service.CDPSourceInstanceService;
import co.lemnisk.common.service.EventDictionaryService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class V2TrackAppBuilderTest {

    private TrackApp trackAppData;

    @Mock
    CDPSourceInstanceService cdpSourceInstanceService;

    @Mock
    EventDictionaryService eventDictionaryService;

    @InjectMocks
     V2TrackAppBuilder builder = new V2TrackAppBuilder();

    @BeforeEach
    void constructData() throws IOException {
        String rawData = getRawData();
        mockDBRequests();
        mockDBRequests2();
        builder.setRawData(rawData);
        trackAppData = builder.getTrackData();
    }

    private String getRawData() throws IOException {
        File file = Util.getFile("/fixtures/analyze_post/v2/track-app.txt");
        return Util.readFileAsString(file);
    }

    private void mockDBRequests() {
        CDPSourceInstance cdpSourceInstance = new CDPSourceInstance();
        cdpSourceInstance.setCdpSourceId(15);
        cdpSourceInstance.setCampaignId(6106);

        when(cdpSourceInstanceService.getCDPSourceInstance(anyInt())).thenReturn(cdpSourceInstance);
    }

    private void mockDBRequests2() {
        CDPCustomEventsDictionary cdpCustomEventsDictionary = new CDPCustomEventsDictionary();
        cdpCustomEventsDictionary.setCampaignId(6106);
        cdpCustomEventsDictionary.setEventName("track");

        when(eventDictionaryService.getEventDictionaryData(anyInt(), anyString(), anyString())).thenReturn(new AbstractMap.SimpleEntry<>(false, cdpCustomEventsDictionary));
    }

    @Test
    @DisplayName("Validate Context Data")
    void validateContextData() {
        var contextData = trackAppData.getContext();
        var libraryData = contextData.getLibrary();
        var userAgentData = contextData.getUserAgent();
        var userDevice = contextData.getDevice();
        var userScreen = contextData.getScreen();
        var userApp = contextData.getApp();

        // Check Remaining Context Data
        assertEquals("49.204.217.145", contextData.getIp());
        assertEquals("6106", contextData.getAccountId());
        assertEquals("15", contextData.getSrcId());

        // Check AppData
        assertEquals("Trackpad", userApp.getName());
        assertEquals("1.4.1", userApp.getVersion());
        assertEquals("10", userApp.getBuild());


    }

    @Test
    @DisplayName("validate Property Data")
    void validatePropertyData(){
        var propertyData = trackAppData.getProperties();

        //checking all data
        assertEquals("android", propertyData.get("platform"));
        assertEquals("single", propertyData.get("view"));
        assertEquals("akhil.bolusani@livspace.com", propertyData.get("user_email"));
        assertEquals("IN", propertyData.get("user_country"));
        assertEquals("10110631", propertyData.get("pid"));
        assertEquals("Hyderabad", propertyData.get("user_city"));
        assertEquals("Mon Mar 07 2022 20:52:58 GMT+0530 (IST)", propertyData.get("timestamp"));
        assertEquals("track", propertyData.get("event_type"));
        assertEquals("BO7487", propertyData.get("user_id"));
        assertEquals("15", propertyData.get("srcid"));
        assertEquals("tasks", propertyData.get("context"));
        assertEquals("trackpad-mobile-app_task-card-selection:card_click", propertyData.get("event_name"));
    }

    @Test
    @DisplayName("Validate Remaining Data")
    void validateRemainingData() {

        assertEquals("f585ee1f-861f-4ade-8e80-f757ff41e3e0", trackAppData.getMessageId());
        assertEquals("viz_6226236f79faa", trackAppData.getId());
        assertEquals("", trackAppData.getReceivedAt());
        assertEquals("Mon Mar 07 2022 20:52:58 GMT+0530 (IST)", trackAppData.getTimestamp());
        assertEquals("", trackAppData.getSentAt());
        assertEquals("", trackAppData.getOriginalTimestamp());
        assertEquals("", trackAppData.getUserId());
        assertEquals("1646666607286", trackAppData.getServerTs());
        assertEquals("track", trackAppData.getType());
        assertEquals("trackpad-mobile-app_task-card-selection:card_click", trackAppData.getEvent());
    }

}