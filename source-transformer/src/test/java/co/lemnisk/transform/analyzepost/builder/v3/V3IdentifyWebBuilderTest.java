package co.lemnisk.transform.analyzepost.builder.v3;

import co.lemnisk.common.Util;
import co.lemnisk.common.avro.model.event.identify.web.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class V3IdentifyWebBuilderTest {

    private IdentifyWeb identifyWebData;

    @BeforeAll
    void  constructData() throws  IOException{
        String rawData = getRawData();
        V3IdentifyWebBuilder builder = new V3IdentifyWebBuilder();
        builder.setRawData(rawData);
        identifyWebData = builder.build();
    }

    private String getRawData() throws IOException {
        File file = Util.getFile("/fixtures/analyze_post/v3/identify-web.txt");
        return Util.readFileAsString(file);
    }

    @Test
    @DisplayName("Validate Context Data")
    void validateContextData() {
        Context context = identifyWebData.getContext();

        ContextLibrary library = context.getLibrary();
        ContextUserAgent userAgent = context.getUserAgent();

        // Check LibraryData

        assertEquals("javascript", library.getName());
        assertEquals("0.01", library.getVersion());

        // Check UserAgentData

        assertEquals("Android", userAgent.getOsType());
        assertEquals("11", userAgent.getOsVersion());

        //check Remaining Data

        assertEquals("180.191.65.146",context.getIp());
        assertEquals("6037", context.getAccountId());
        assertEquals("203", context.getSrcId());
    }

    @Test
    @DisplayName("Validate Customer Properties Data")
    void validateCustomerPropertiesData() {
        Map<CharSequence, Object> customerPropertiesData = identifyWebData.getCustomerProperties();
        assertEquals("Sociable", customerPropertiesData.get("persona"));
        assertEquals("yes", customerPropertiesData.get("privacyPolicy"));
        assertEquals("Rose Cartujano", customerPropertiesData.get("fullName"));
        assertEquals("manual-sign", customerPropertiesData.get("signInMethod"));
    }

    @Test
    @DisplayName("Validate Page Data")
    void validatePageData(){
        Page userPage =identifyWebData.getPage();
        assertEquals("/result",userPage.getPath());
        assertEquals("https://www.canvasofyou.com/quiz",userPage.getReferrer());
        assertEquals("?persona=Sociable",userPage.getSearch());
        assertEquals("https://www.canvasofyou.com/result?persona=Sociable",userPage.getUrl());
    }


    @Test
    @DisplayName("Validate OtherIds")
    void validateOtherIds(){
        Map<CharSequence, Object> customerOtherIds =  identifyWebData.getOtherIds();
        assertEquals("fb.1.1640619048621.1888692129",customerOtherIds.get("fbp"));
        assertEquals("GA1.1.754570807.1640619047",customerOtherIds.get("ga"));
        assertEquals("cbade99e706fcfa8c3d7aee781f1176953fc976654b24e465a07c94b0f128a1b",customerOtherIds.get("email"));
    }


    @Test
    @DisplayName("Validate Remaining Data")
    void validateRemainingData() {
        assertEquals("e91f535b-7de1-49d8-aabc-bf92bfc047e3",identifyWebData.getMessageId());
        assertEquals("tjwcn0ru0kov22mm46yc",identifyWebData.getWriteKey());
        assertEquals("viz_61c9dc348440b",identifyWebData.getId());
        assertEquals("identify",identifyWebData.getType());
        assertEquals("1640619417647",identifyWebData.getReceivedAt());
        assertEquals("userId",identifyWebData.getUserId());
        assertEquals("1640619417647",identifyWebData.getServerTs());
        assertEquals("1640619417062",identifyWebData.getOriginalTimestamp());

    }


}
