package co.lemnisk.transform.analyzepost.builder.v2;

import co.lemnisk.common.Util;
import co.lemnisk.common.avro.model.event.screen.Screen;
import co.lemnisk.common.model.CDPSourceInstance;
import co.lemnisk.common.service.CDPSourceInstanceService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class V2ScreenBuilderTest {

    private Screen screenAppData;

    @Mock
    CDPSourceInstanceService cdpSourceInstanceService;

    @InjectMocks
    V2ScreenBuilder builder = new V2ScreenBuilder();

    @BeforeEach
    void constructData() throws IOException {
        String rawData = getRawData();
        mockDBRequests();
        builder.setRawData(rawData);
        screenAppData = builder.getScreenData();
    }

    private String getRawData() throws IOException {
        File file = Util.getFile("/fixtures/analyze_post/v2/screen-app.txt");
        return Util.readFileAsString(file);
    }

    private void mockDBRequests() {
        CDPSourceInstance cdpSourceInstance = new CDPSourceInstance();
        cdpSourceInstance.setCdpSourceId(15);
        cdpSourceInstance.setCampaignId(6106);

        when(cdpSourceInstanceService.getCDPSourceInstance(anyInt())).thenReturn(cdpSourceInstance);
    }

    @Test
    @DisplayName("Validate Context Data")
    void validateContextData() {
        var contextData = screenAppData.getContext();
        var libraryData = contextData.getLibrary();
        var userAgentData = contextData.getUserAgent();
        var userDevice = contextData.getDevice();
        var userScreen = contextData.getScreen();
        var userApp = contextData.getApp();


        // Check Remaining Context Data
        assertEquals("103.133.229.239", contextData.getIp());
        assertEquals("6081", contextData.getAccountId());
        assertEquals("8", contextData.getSrcId());

        // Check AppData
        assertEquals("21.7", userApp.getVersion());
        assertEquals("21.7", userApp.getBuild());

    }

    @Test
    @DisplayName("validate Property Data")
    void validatePropertyData(){
        var propertyData = screenAppData.getProperties();

        //checking all data
        assertEquals("ios-app", propertyData.get("source"));
        assertEquals("screen", propertyData.get("event_type"));
        assertEquals("Santosh", propertyData.get("name"));
        assertEquals("8", propertyData.get("srcid"));
        assertEquals("f7578da82b70795a93c1e1e13344a8f9b7aba0467a3233a1e0956360b390197d", propertyData.get("email"));
        assertEquals("ios", propertyData.get("OS"));
        assertEquals("hub-app_page:page_viewed", propertyData.get("input_event_name"));
        assertEquals("Timeline", propertyData.get("title"));
        assertEquals("21.7", propertyData.get("version"));
        assertEquals("2022-03-22T18:21:24.881Z", propertyData.get("originalTimestamp"));
        assertEquals("CX1163403", propertyData.get("userId"));
    }

    @Test
    @DisplayName("Validate Remaining Data")
    void validateRemainingData() {

        assertEquals("82005CC2-203E-462F-82A2-17437E96B793", screenAppData.getMessageId());
        assertEquals("viz_a_8A7CF7B7-170C-4351-A8A8-EA6C98991D16", screenAppData.getId());
        assertEquals("", screenAppData.getReceivedAt());
        assertEquals("", screenAppData.getTimestamp());
        assertEquals("2022-03-22T18:21:24.902Z", screenAppData.getSentAt());
        assertEquals("2022-03-22T18:21:24.881Z", screenAppData.getOriginalTimestamp());
        assertEquals("CX1163403", screenAppData.getUserId());
        assertEquals("1647973285024", screenAppData.getServerTs());
        assertEquals("screen", screenAppData.getType());
    }

}