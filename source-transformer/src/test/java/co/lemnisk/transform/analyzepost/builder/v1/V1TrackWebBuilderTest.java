package co.lemnisk.transform.analyzepost.builder.v1;

import co.lemnisk.common.service.EventDictionaryService;
import co.lemnisk.common.Util;
import co.lemnisk.common.model.CDPCustomEventsDictionary;
import co.lemnisk.common.avro.model.event.track.web.TrackWeb;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class V1TrackWebBuilderTest {
    private TrackWeb trackWebData;

    @Mock
    EventDictionaryService eventDictionaryService;

    @InjectMocks
    V1TrackWebBuilder builder = new V1TrackWebBuilder();

    @BeforeEach
    void constructData() throws IOException {
        String rawData = getRawData();
        mockDBRequests();
        builder.setData(rawData);
        trackWebData = builder.build();
    }

    private String getRawData() throws IOException {
        File file = Util.getFile("/fixtures/analyze_post/v1/track-web.txt");
        return Util.readFileAsString(file);
    }

    private void mockDBRequests() {
        CDPCustomEventsDictionary cdpCustomEventsDictionary = new CDPCustomEventsDictionary();
        cdpCustomEventsDictionary.setId(1);
        cdpCustomEventsDictionary.setCampaignId(5979);
        cdpCustomEventsDictionary.setEventName("productClick");
        cdpCustomEventsDictionary.setLemEventName("LemniskTestEvent");

        when(eventDictionaryService.getEventDictionaryData(anyInt(), anyString(), anyString())).thenReturn(new AbstractMap.SimpleEntry<>(false, cdpCustomEventsDictionary));
    }


    @Test
    @DisplayName("Validate Context Data")
    void validateContextData() {
        var contextData = trackWebData.getContext();
        var libraryData = contextData.getLibrary();
        var userAgentData = contextData.getUserAgent();
        var utmData = contextData.getUtm();
        var pageData = contextData.getPage();

        // Check Library Data
        assertEquals("javascript", libraryData.getName());
        assertEquals("0.01", libraryData.getVersion());

        // Check UserAgent Data
        assertEquals("iOS", userAgentData.getOsType());
        assertEquals("15", userAgentData.getOsVersion());
        assertEquals("Safari", userAgentData.getBrowser());
        assertEquals("", userAgentData.getUa());
        assertEquals("MOBILE", userAgentData.getDeviceType());

        // Check UTM Data
        assertEquals("", utmData.getCampaign());
        assertEquals("", utmData.getSource());
        assertEquals("", utmData.getMedium());
        assertEquals("", utmData.getTerm());
        assertEquals("", utmData.getContent());

        // Check Page Data
        assertEquals("", pageData.getSearch());
        assertEquals("", pageData.getTitle());
        assertEquals("", pageData.getUrl());
        assertEquals("", pageData.getPath());
        assertEquals("", pageData.getReferrer());

        // Check Remaining Context Data
        assertEquals("106.193.159.21", contextData.getIp());
        assertEquals("5979", contextData.getAccountId());
        assertEquals("179", contextData.getSrcId());
    }

//    @Test
//    @DisplayName("Validate Properties Data")
//    void validatePropertiesData () {
//        Map<CharSequence, Object> propertiesData = trackWebData.getProperties();
//        assertEquals("STD (150 CC)", propertiesData.get("Variant"));
//        assertEquals("https://uatsf.hdfcergo.com/UTesting/TWRevamp/Integration/IncompleteTransaction/", propertiesData.get("DropOffURL"));
//    }

    @Test
    @DisplayName("Validate OtherIds Data")
    void validateOtherIdsData () {
        Map<CharSequence, Object> otherIdsData = trackWebData.getOtherIds();
        assertEquals("fb.1.1640591662341.1526845501", otherIdsData.get("fbp"));
        assertEquals("GA1.2.391492456.1640591662", otherIdsData.get("ga"));
    }

    @Test
    @DisplayName("Validate Remaining Data")
    void validateRemainingData () {
        assertEquals("350a9a02-907f-47ca-ad83-1fdded8cd089", trackWebData.getMessageId());
        assertEquals("", trackWebData.getSentAt());
        assertEquals("1640619413273", trackWebData.getOriginalTimestamp());
        assertEquals("viz_61c971619a30a", trackWebData.getId());
        assertEquals("track", trackWebData.getType());
        assertEquals("TvSrqZgFSm2zeVg8", trackWebData.getWriteKey());
        assertEquals("", trackWebData.getUserId());
        assertEquals("1640619415079", trackWebData.getServerTs());
        assertEquals("LemniskTestEvent", trackWebData.getLemEvent());
        assertEquals("productClick", trackWebData.getEvent());
        assertEquals("1640619415079", trackWebData.getReceivedAt());
        assertEquals("1640619415079", trackWebData.getTimestamp());
        assertEquals(false, trackWebData.getIsStandardEvent());
    }
}