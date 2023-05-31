package co.lemnisk.transform.analyzepost.builder.v3;
import co.lemnisk.common.Util;
import co.lemnisk.common.avro.model.event.track.web.*;
import co.lemnisk.common.model.CDPCustomEventsDictionary;
import co.lemnisk.common.service.EventDictionaryService;
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
class V3TrackWebBuilderTest {
   private TrackWeb trackWebData;

    @Mock
    EventDictionaryService eventDictionaryService;

    @InjectMocks
    V3TrackWebBuilder builder = new V3TrackWebBuilder();


   @BeforeEach
    void constructData() throws IOException {
        String rawData = getRawData();
        builder.setRawData(rawData);
        mockDBRequests();
        trackWebData = builder.build();
   }

    private String getRawData() throws IOException {
        File file = Util.getFile("/fixtures/analyze_post/v3/track-web.txt");
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
        Context context = trackWebData.getContext();
        ContextUserAgent userAgent=context.getUserAgent();
        ContextLibrary library = context.getLibrary();
        ContextPage userPage = context.getPage();

        // Check LibraryData

        assertEquals("javascript", library.getName());
        assertEquals("0.01", library.getVersion());

        //check userAgent

        assertEquals("Android", userAgent.getOsType());
        assertEquals("10", userAgent.getOsVersion());
        assertEquals("Chrome", userAgent.getBrowser());
        assertEquals("Mozilla", userAgent.getUa());
        assertEquals("MOBILE", userAgent.getDeviceType());

        //check Remaining Data
        assertEquals("136.158.56.204",context.getIp());
        assertEquals("VIZVRM6037", context.getAccountId());
        assertEquals("203", context.getSrcId());

        //check page data
        assertEquals("/result",userPage.getPath());
        assertEquals("https://m.facebook.com/v9.0/dialog/oauth/read/",userPage.getReferrer());
        assertEquals("?persona=Passionate",userPage.getSearch());
        assertEquals("Canvas Of You",userPage.getTitle());
        assertEquals("https://canvasofyou.com/result?persona=Passionate#",userPage.getUrl());
    }



    @Test
    @DisplayName("Validate OtherIds")
    void validateOtherIds(){
        Map<CharSequence, Object> customerOtherIds =  trackWebData.getOtherIds();
        assertEquals("fb.1.1640601660497.1001365969",customerOtherIds.get("fbp"));
        assertEquals ("fb.1.1640601660495.IwAR0xSdU6PuHNi6lp5dvUtwIkAsHe1To6Xw7TZ7Wb5lYj1aedP1YTFnIEahY",customerOtherIds.get("fbc"));
        assertEquals("GA1.1.209339422.1640601660",customerOtherIds.get("ga"));
    }

//    @Test
//    @DisplayName("Validate Properties Data")
//    void validatePropertiesData() {
//        Map<CharSequence, Object> propertiesData = trackWebData.getProperties();
//        assertEquals("Result Page - Persona - Passionate",propertiesData.get("on_page"));
//        assertEquals("Email",propertiesData.get("type"));
//    }

    @Test
    @DisplayName("Validate Remaining Data")
    void validateRemainingData() {
        assertEquals("tjwcn0ru0kov22mm46yc",trackWebData.getWriteKey());
        assertEquals("7d29cf07-71ff-48b9-bdf1-2603f70cc1a4",trackWebData.getMessageId());
        assertEquals("viz_61c99933204ce",trackWebData.getId());
        assertEquals("track",trackWebData.getType());
        assertEquals("share",trackWebData.getEvent());
        assertEquals("1640601991544",trackWebData.getReceivedAt());
        assertEquals("userId",trackWebData.getUserId());
        assertEquals("1640601991544",trackWebData.getServerTs());
        assertEquals("1640601989812",trackWebData.getOriginalTimestamp());
    }
}