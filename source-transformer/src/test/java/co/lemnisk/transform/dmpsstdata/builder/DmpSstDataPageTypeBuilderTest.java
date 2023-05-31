package co.lemnisk.transform.dmpsstdata.builder;

import co.lemnisk.common.Util;
import co.lemnisk.common.avro.model.event.page.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DmpSstDataPageTypeBuilderTest {

    private DmpSstDataPageTypeBuilder builder;

    @BeforeAll
    void constructData() throws IOException {

        String filePath = "/fixtures/dmpsstdata/page.json";
        HashMap<String, String> mapData = Util.jsonFileToMap(filePath);

        builder = new DmpSstDataPageTypeBuilder(mapData, "");
        builder.constructData();
    }

    @Test
    @DisplayName("Validate Context Data")
    void ValidateContextData () {
        Page data = builder.getData();

        Context context = data.getContext();
        ContextLibrary library = context.getLibrary();
        ContextUserAgent userAgent = context.getUserAgent();
        ContextUtm utm = context.getUtm();

        // Check LibraryData
        assertEquals("smarttag", library.getName());
        assertEquals("0.29v", library.getVersion());

        // Check UserAgent
        assertEquals("MOBILE", userAgent.getDeviceType());
//        assertEquals("viz_61cc23a6d0ae4", userAgent.getOsType());
//        assertEquals("Linux", userAgent.getBrowser());
        assertEquals("Linux", userAgent.getOsVersion());
        assertEquals("Mozilla", userAgent.getUa());

        // Check UTM
        assertEquals("poojaroom", utm.getCampaign());
        assertEquals("facebook", utm.getSource());
        assertEquals("post", utm.getMedium());
        assertEquals("29092021", utm.getTerm());
//        assertEquals("poojaroom", utm.getContent());

        assertEquals("127.0.0.1", context.getIp());
        assertEquals("6081", context.getAccountId());
        assertEquals("", context.getSrcId());
    }

    @Test
    @DisplayName("Validate OtherIds Data")
    void ValidateOtherIdsData () {
        Page data = builder.getData();
        Map<CharSequence, Object> otherIds = data.getOtherIds();

        assertEquals("sample-ga-value", otherIds.get("_ga"));
        assertEquals("sample-fbp-value", otherIds.get("_fbp"));
        assertEquals("some-hash-code-for-email", otherIds.get("email"));
        assertEquals("some-hash-code-for-phone", otherIds.get("phone"));
    }

    @Test
    @DisplayName("Validate Property Data")
    void ValidatePropertyData () {
        Page data = builder.getData();

        Map<CharSequence, Object> properties = data.getProperties();

        assertEquals("/in/magazine/pooja-mandir-designs-fashion", properties.get("p_path"));
        assertEquals("page title", properties.get("p_title"));
        assertEquals("https://www.test.com", properties.get("p_url"));
        assertEquals("https://l.facebook.com/", properties.get("p_referrer"));
    }

    @Test
    @DisplayName("Validate Remaining Data")
    void ValidateRemainingData () {
        Page data = builder.getData();

        assertEquals("0ea0c0f3-571b-4f58-8e6f-d87b67f2dda8", data.getMessageId());
        assertEquals("2021-12-12T07:07:56.000Z", data.getSentAt());
        assertEquals("2021-12-12T07:07:56.000Z", data.getOriginalTimestamp());
        assertEquals("viz_61b59fccfcd9e", data.getId());
        assertEquals(builder.eventType(), data.getType());
        assertEquals("s6b9tbu0y0b5xxtqtiv4", data.getWriteKey());
    }

}