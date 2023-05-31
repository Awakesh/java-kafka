package co.lemnisk.transform.dmpsstdata.builder;

import co.lemnisk.common.Util;
import co.lemnisk.common.avro.model.event.identify.web.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DmpSstDataIdentifyTypeBuilderTest {

    private DmpSstDataIdentifyTypeBuilder builder;

    @BeforeAll
    void constructData() throws IOException {

        String filePath = "/fixtures/dmpsstdata/identify-web.json";
        HashMap<String, String> mapData = Util.jsonFileToMap(filePath);

        builder = new DmpSstDataIdentifyTypeBuilder(mapData, "");
        builder.constructData();
    }

    @Test
    @DisplayName("Validate Identify Data")
    void ValidateContextData () {
        IdentifyWeb data = builder.getData();

        Context context = data.getContext();

        ContextLibrary library = context.getLibrary();
        ContextUserAgent userAgent = context.getUserAgent();

        // Check LibraryData
        assertEquals("smarttag", library.getName());
        assertEquals("0.06v", library.getVersion());

        // Check UserAgent
        assertEquals("MOBILE", userAgent.getDeviceType());
        assertEquals("Android", userAgent.getOsType());
//        assertEquals("Linux", userAgent.getBrowser());
        assertEquals("Linux", userAgent.getOsVersion());
        assertEquals("Mozilla", userAgent.getUa());

        assertEquals("127.0.0.1", context.getIp());
        assertEquals("6106", context.getAccountId());
        assertEquals("", context.getSrcId());
    }

    @Test
    @DisplayName("Validate OtherIds Data")
    void ValidateOtherIdsData () {
        IdentifyWeb data = builder.getData();
        Map<CharSequence, Object> otherIds = data.getOtherIds();

        assertEquals("sample-ga-value", otherIds.get("_ga"));
        assertEquals("sample-fbp-value", otherIds.get("_fbp"));
        assertEquals("some-hash-code-for-email", otherIds.get("email"));
        assertEquals("some-hash-code-for-phone", otherIds.get("phone"));
    }

    @Test
    @DisplayName("Validate Property Data")
    void ValidateCustomerPropertyData () {
        IdentifyWeb data = builder.getData();

        Map<CharSequence, Object> properties = data.getCustomerProperties();

        assertEquals("266", properties.get("i_managerId"));
        assertEquals("BO5420", properties.get("i_userid"));
        assertEquals("Bangalore", properties.get("i_city"));
        assertEquals("John Doe", properties.get("i_name"));
    }

    @Test
    @DisplayName("Validate Page Data")
    void ValidatePageData () {
        IdentifyWeb data = builder.getData();

        Page page = data.getPage();

        assertEquals("Livspace Studio - Livspace Studio Platform.", page.getTitle());
        assertEquals("/projects/1050664", page.getPath());
        assertEquals("android-app://com.google.android.gm/", page.getReferrer());
        assertEquals("https://canvas.livspace.com/projects/1050664", page.getUrl());
//        assertEquals("", page.getSearch());
    }

    @Test
    @DisplayName("Validate Remaining Data")
    void ValidateRemainingData () {
        IdentifyWeb data = builder.getData();

        assertEquals("f79c5181-0920-486a-be0f-f2d327824226", data.getMessageId());
        assertEquals("2022-01-16T07:11:55.000Z", data.getSentAt());
        assertEquals("2022-01-16T07:11:55.000Z", data.getOriginalTimestamp());
//        assertEquals("", data.getServerTs());
        assertEquals("viz_613211e85402e", data.getId());
        assertEquals(builder.eventType(), data.getType());
        assertEquals("s6b9tbu0y0b5xxtqtiv4", data.getWriteKey());
    }

}