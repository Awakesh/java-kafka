package co.lemnisk.transform.analyzepost.builder.v3;
import co.lemnisk.common.Util;
import co.lemnisk.common.avro.model.event.track.app.*;
import co.lemnisk.common.model.CDPCustomEventsDictionary;
import co.lemnisk.common.model.CDPSourceInstance;
import co.lemnisk.common.service.CDPSourceInstanceService;
import co.lemnisk.common.service.EventDictionaryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class V3TrackAppBuilderTest {

  private TrackApp trackAppData;

    @Mock
    EventDictionaryService eventDictionaryService;

    @InjectMocks
    V3TrackAppBuilder builder= new V3TrackAppBuilder();

    @BeforeEach
    void constructData() throws IOException {
        String rawData = getRawData();
        mockDBRequests();
        builder.setRawData(rawData);
        trackAppData = builder.build();
    }

    private String getRawData() throws IOException {
        File file = Util.getFile("/fixtures/analyze_post/v3/track-app.txt");
        return Util.readFileAsString(file);
    }

    private void mockDBRequests() {
        CDPCustomEventsDictionary cdpCustomEventsDictionary = new CDPCustomEventsDictionary();
        cdpCustomEventsDictionary.setCampaignId(6155);
        cdpCustomEventsDictionary.setEventName("track");

        when(eventDictionaryService.getEventDictionaryData(anyInt(), anyString(), anyString())).thenReturn(new AbstractMap.SimpleEntry<>(false, cdpCustomEventsDictionary));
    }


    @Test
    @DisplayName("Validate Context Data")
    void validateContextData() {
        Context context = trackAppData.getContext();
        ContextLibrary library =context.getLibrary();
        ContextUserAgent userAgent =context.getUserAgent();
        ContextApp userApp = context.getApp();
        ContextDevice userDevice = context.getDevice();
        ContextScreen userScreen=context.getScreen();

        // Check LibraryData
        assertEquals("lemnisk-android", library.getName());
        assertEquals("1.1.7.1", library.getVersion());

        // Check UserAgentData
//        assertEquals("android", userAgent.getOsType());
//        assertEquals("10", userAgent.getOsVersion());

        // Check Remaining Context Data
        assertEquals("190.62.6.136", context.getIp());
        assertEquals("6155", context.getAccountId());
        assertEquals("182", context.getSrcId());

        // Check AppData
        assertEquals("F1 stc Saudi Arabian GP", userApp.getName());
        assertEquals("1.0.21", userApp.getVersion());
        assertEquals("45", userApp.getBuild());

        // Check DeviceData
        assertEquals("defdd88cb13ddaa9",userDevice.getId());
        assertEquals(false, userDevice.getAdTrackingEnabled());
        assertEquals("d181d444-5c4a-46c0-82d0-6118276b5873",userDevice.getAdvertisingId());
        assertEquals("android",userDevice.getType());
        assertEquals("samsung",userDevice.getManufacturer());
//        assertEquals("SM-G975U",userDevice.getModel());
        assertEquals("",userDevice.getToken());

        // Check ScreenData
        assertEquals("2730", userScreen.getHeight());
        assertEquals("3.5", userScreen.getDensity());
        assertEquals("1440", userScreen.getWidth());
    }

//    @Test
//    @DisplayName("validate Property Data")
//    void validatePropertyData(){
//        Map<CharSequence, Object> propertiesData = trackAppData.getProperties();
//        assertEquals("Carousel", propertiesData.get("labelType"));
//        assertEquals("en", propertiesData.get("language"));
//        assertEquals("1998-10-29T00:00:00.000Z",propertiesData.get("dateOfBirth"));
//        assertEquals("3",propertiesData.get("podiums"));
//        assertEquals("100", propertiesData.get("grandPrixEntered"));
//        assertEquals("0",propertiesData.get("worldChampionships"));
//        assertEquals("1",propertiesData.get("highestGridPosition"));
//        assertEquals("Montreal, Canada",propertiesData.get( "placeOfBirth"));
//        assertEquals("34",propertiesData.get("driverPoints"));
//        assertEquals("13",propertiesData.get( "driverRank"));
//    }

    @Test
    @DisplayName("Validate OtherIds")
    void validateOtherIds(){
        Map<CharSequence, Object> customerOtherIds =  trackAppData.getOtherIds();
        assertEquals("defdd88cb13ddaa9",customerOtherIds.get("trackerId"));
    }

    @Test
    @DisplayName("Validate Remaining Data")
    void validateRemainingData() {
        assertEquals("7491b3c5-8ccc-4442-ac86-e4eb4b575e3f", trackAppData.getMessageId());
        assertEquals("defdd88cb13ddaa9", trackAppData.getId());
        assertEquals("1640528855386", trackAppData.getReceivedAt());
        assertEquals("2021-12-26T08:27:34.735-06:00", trackAppData.getTimestamp());
        assertEquals("2021-12-26T08:27:34.735-06:00", trackAppData.getSentAt());
        assertEquals("2021-12-26T08:27:34.735-06:00", trackAppData.getOriginalTimestamp());
        assertEquals("", trackAppData.getUserId());
        assertEquals("1640528855386", trackAppData.getServerTs());
        assertEquals("track", trackAppData.getType());
        assertEquals("userClicked", trackAppData.getEvent());
        assertEquals("27hcnu4npvbqh5e75zgg", trackAppData.getWriteKey());
    }
    }




