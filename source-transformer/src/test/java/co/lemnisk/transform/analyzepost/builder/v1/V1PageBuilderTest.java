package co.lemnisk.transform.analyzepost.builder.v1;

import co.lemnisk.common.Util;
import co.lemnisk.common.avro.model.event.page.Page;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class V1PageBuilderTest {
    private Page pageData;

    @BeforeAll
    void constructData() throws IOException {
        String rawData = getRawData();
        V1PageBuilder builder = new V1PageBuilder(rawData);
        pageData = builder.build();
    }

    private String getRawData() throws IOException {
        File file = Util.getFile("/fixtures/analyze_post/v1/page.txt");
        return Util.readFileAsString(file);
    }

//    @Test
//    @DisplayName("Invalid Input JSON String will return in Runtime Exceptions")
//    void invalidInputJsonStrThrowsException() {
//        String rawData = "";
//        assertThrows(NullPointerException.class, () -> {
//            new V1PageBuilder(rawData).build();
//        });
//    }

    @Test
    @DisplayName("Validate Context Data")
    void validateContextData() {
        var contextData = pageData.getContext();
        var libraryData = contextData.getLibrary();
        var userAgentData = contextData.getUserAgent();
        var utmData = contextData.getUtm();

        // Check Library Data
        assertEquals("javascript", libraryData.getName());
        assertEquals("0.01", libraryData.getVersion());

        // Check UserAgent Data
        assertEquals("iOS", userAgentData.getOsType());
        assertEquals("14", userAgentData.getOsVersion());
        assertEquals("Safari", userAgentData.getBrowser());
        assertEquals("", userAgentData.getUa());
        assertEquals("MOBILE", userAgentData.getDeviceType());

        // Check UTM Data
        assertEquals("", utmData.getCampaign());
        assertEquals("", utmData.getSource());
        assertEquals("", utmData.getMedium());
        assertEquals("", utmData.getTerm());
        assertEquals("", utmData.getContent());

        // Check Remaining Context Data
        assertEquals("168.149.15.113", contextData.getIp());
        assertEquals("6155", contextData.getAccountId());
        assertEquals("167", contextData.getSrcId());
    }

    @Test
    @DisplayName("Validate Properties Data")
    void validatePropertiesData () {
        Map<CharSequence, Object> propertiesData = pageData.getProperties();
        assertEquals("/", propertiesData.get("path"));
        assertEquals("https://www.google.com", propertiesData.get("referrer"));
        assertEquals("Home", propertiesData.get("name"));
        assertEquals("سباق جائزة السعودية الكبرى للفورمولا stc1 لعام 2021 في مدينة جدة", propertiesData.get("title"));
        assertEquals("https://www.saudiarabiangp.com/", propertiesData.get("url"));
    }

    @Test
    @DisplayName("Validate OtherIds Data")
    void validateOtherIdsData () {
        Map<CharSequence, Object> otherIdsData = pageData.getOtherIds();
        assertEquals("fb.1.1640529017060.1769134756", otherIdsData.get("fbp"));
        assertEquals("GA1.2.118403509.1640529017", otherIdsData.get("ga"));
    }

    @Test
    @DisplayName("Validate Remaining Data")
    void validateRemainingData () {
        assertEquals("65d8df4f-c65b-4aa0-81da-b9def9a547a4", pageData.getMessageId());
        assertEquals("", pageData.getSentAt());
        assertEquals("1640529017754", pageData.getOriginalTimestamp());
        assertEquals("viz_61c87c790d3d5", pageData.getId());
        assertEquals("page", pageData.getType());
        assertEquals("mEnd4Nfr4Bw3xCrc", pageData.getWriteKey());
        assertEquals("", pageData.getUserId());
        assertEquals("1640529018103", pageData.getServerTs());
        assertEquals("1640529018103", pageData.getReceivedAt());
        assertEquals("1640529018103", pageData.getTimestamp());
        assertEquals("Home", pageData.getName());
    }
}