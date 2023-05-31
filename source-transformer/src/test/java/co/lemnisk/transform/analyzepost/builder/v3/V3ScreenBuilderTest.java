package co.lemnisk.transform.analyzepost.builder.v3;
import co.lemnisk.common.Util;
import co.lemnisk.common.avro.model.event.screen.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class V3ScreenBuilderTest {
   private  V3ScreenBuilder builder;
   private Screen screenAppData;

    @BeforeAll
    void constructData() throws IOException {
        String rawData = getRawData();
        V3ScreenBuilder builder = new V3ScreenBuilder();
        builder.setRawData(rawData);
        screenAppData = builder.build();
    }

    private String getRawData() throws IOException {
        File file = Util.getFile("/fixtures/analyze_post/v3/screen.txt");
        return Util.readFileAsString(file);
    }

    @Test
    @DisplayName("Validate Context Data")
    void validateContextData() {
        Context context = screenAppData.getContext();
        ContextLibrary library = context.getLibrary();
        ContextUserAgent userAgent = context.getUserAgent();
        ContextScreen userScreen = context.getScreen();
        ContextDevice userDevice = context.getDevice();
        ContextApp userApp = context.getApp();

        // Check AppData
        assertEquals("Slider", userApp.getName());
        assertEquals("1.0.21", userApp.getVersion());
        assertEquals("45", userApp.getBuild());

        // Check LibraryData
        assertEquals("lemnisk-android", library.getName());
        assertEquals("1.1.7.1", library.getVersion());

        //  Check ScreenData
        assertEquals("2169", userScreen.getHeight());
        assertEquals("2.625", userScreen.getDensity());
        assertEquals("1768", userScreen.getWidth());


        // Check UserAgentData
        assertEquals("Android", userAgent.getOsType());
        assertEquals("11", userAgent.getOsVersion());

        // Check DeviceData
        assertEquals(false, userDevice.getAdTrackingEnabled());
        assertEquals("053459aa-6bdc-4529-a10c-e62e41d2001a", userDevice.getAdvertisingId());
//        assertEquals("SM-F926B", userDevice.getModel());
        assertEquals("814cf90dfa24151f", userDevice.getId());
        assertEquals("android", userDevice.getType());
        assertEquals("samsung", userDevice.getManufacturer());
        assertEquals("", userDevice.getToken());

        // Check Remaining Context Data
        assertEquals("142.154.115.223", context.getIp());
        assertEquals("6155", context.getAccountId());
        assertEquals("182", context.getSrcId());

    }

    @Test
    @DisplayName("validate OtherIds Data")
    void validateOtherIdsData(){
        Map<CharSequence, Object> customerOtherIds =  screenAppData.getOtherIds();
        assertEquals("814cf90dfa24151f",customerOtherIds.get("trackerId"));
        assertEquals("android",customerOtherIds.get("OS"));
    }

    @Test
    @DisplayName("validate Property Data")
    void validatePropertyData() {
        Map<CharSequence, Object>  propertiesData =  screenAppData.getProperties();
        assertEquals("ar",propertiesData.get("language"));
    }

    @Test
    @DisplayName("Validate Remaining Data")
    void validateRemainingData() {
        assertEquals("79a43d84-962e-41e4-9418-6334be8ec89f",screenAppData.getMessageId());
        assertEquals("814cf90dfa24151f", screenAppData.getId());
        assertEquals("1640531296058", screenAppData.getReceivedAt());
        assertEquals("2021-12-26T18:08:14.390 03:00", screenAppData.getTimestamp());
        assertEquals("2021-12-26T18:08:14.390 03:00", screenAppData.getSentAt());
        assertEquals("2021-12-26T18:08:14.390 03:00", screenAppData.getOriginalTimestamp());
        assertEquals("1640531296058", screenAppData.getServerTs());
        assertEquals("screen", screenAppData.getType());
        assertEquals("27hcnu4npvbqh5e75zgg", screenAppData.getWriteKey());
    }
}
