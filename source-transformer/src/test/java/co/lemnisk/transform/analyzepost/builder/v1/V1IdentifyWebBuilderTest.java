package co.lemnisk.transform.analyzepost.builder.v1;

import co.lemnisk.common.Util;
import co.lemnisk.common.avro.model.event.identify.web.IdentifyWeb;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class V1IdentifyWebBuilderTest {

    private IdentifyWeb identifyWebData;

    @BeforeAll
    void constructData() throws IOException {
        String rawData = getRawData();
        V1IdentifyWebBuilder builder = new V1IdentifyWebBuilder(rawData);
        identifyWebData = builder.build();
    }

    private String getRawData() throws IOException {
        File file = Util.getFile("/fixtures/analyze_post/v1/identify-web.txt");
        return Util.readFileAsString(file);
    }

//    @Test
//    @DisplayName("Invalid Input JSON String will return in Runtime Exceptions")
//    void invalidInputJsonStrThrowsException() {
//        String rawData = "";
//        assertThrows(NullPointerException.class, () -> {
//            new V1IdentifyWebBuilder(rawData).build();
//        });
//    }

    @Test
    @DisplayName("Validate Page Data")
    void validatePageData() {
        var pageData = identifyWebData.getPage();
        assertEquals("", pageData.getSearch());
        assertEquals("", pageData.getTitle());
        assertEquals("", pageData.getUrl());
        assertEquals("", pageData.getPath());
        assertEquals("", pageData.getReferrer());
    }

    @Test
    @DisplayName("Validate Context Data")
    void validateContextData() {
        var contextData = identifyWebData.getContext();
        var libraryData = contextData.getLibrary();
        var userAgentData = contextData.getUserAgent();

        // Check LibraryData
        assertEquals("javascript", libraryData.getName());
        assertEquals("0.01", libraryData.getVersion());

        // Check UserAgentData
        assertEquals("Android", userAgentData.getOsType());
        assertEquals("11", userAgentData.getOsVersion());
        assertEquals("Chrome", userAgentData.getBrowser());
        assertEquals("", userAgentData.getUa());
        assertEquals("MOBILE", userAgentData.getDeviceType());

        // Check Remaining Context Data
        assertEquals("106.222.81.47", contextData.getIp());
        assertEquals("6184", contextData.getAccountId());
        assertEquals("161", contextData.getSrcId());

    }

    @Test
    @DisplayName("Validate Customer Properties Data")
    void validateCustomerPropertiesData () {
        Map<CharSequence, Object> customerPropertiesData = identifyWebData.getCustomerProperties();
        assertEquals("Activ Health V2Platinum - Enhanced", customerPropertiesData.get("Product"));
        assertEquals("sachin@1434", customerPropertiesData.get("userName"));
    }

    @Test
    @DisplayName("Validate OtherIds Data")
    void validateOtherIdsData () {
        Map<CharSequence, Object> otherIdsData = identifyWebData.getOtherIds();
        assertEquals("fb.1.1625213164445.8354188", otherIdsData.get("fbp"));
        assertEquals("GA1.2.1778940349.1625213162", otherIdsData.get("ga"));
    }

    @Test
    @DisplayName("Validate Remaining Data")
    void validateRemainingData () {
        assertEquals("647292ba-6e2a-48c5-8fbc-725945248e23", identifyWebData.getMessageId());
        assertEquals("", identifyWebData.getSentAt());
        assertEquals("1640601991250", identifyWebData.getOriginalTimestamp());
        assertEquals("viz_6061e5f10182f", identifyWebData.getId());
        assertEquals("identify", identifyWebData.getType());
        assertEquals("9EbJ0IIVWteMSFno", identifyWebData.getWriteKey());
        assertEquals("", identifyWebData.getUserId());
        assertEquals("1640601991850", identifyWebData.getServerTs());
        assertEquals("1640601991850", identifyWebData.getReceivedAt());
        assertEquals("1640601991850", identifyWebData.getTimestamp());
    }

}