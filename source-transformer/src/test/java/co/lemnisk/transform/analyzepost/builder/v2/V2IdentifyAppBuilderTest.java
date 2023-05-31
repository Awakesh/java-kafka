package co.lemnisk.transform.analyzepost.builder.v2;

import co.lemnisk.common.Util;
import co.lemnisk.common.avro.model.event.identify.app.IdentifyApp;
import co.lemnisk.common.model.CDPCustomEventsDictionary;
import co.lemnisk.common.model.CDPSourceInstance;
import co.lemnisk.common.service.CDPSourceInstanceService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class V2IdentifyAppBuilderTest {

    private IdentifyApp identifyAppData;

    @Mock
    CDPSourceInstanceService cdpSourceInstanceService;

    @InjectMocks
    V2IdentifyAppBuilder builder = new V2IdentifyAppBuilder();


    @BeforeEach
    void constructData() throws IOException {
        String rawData = getRawData();
        mockDBRequests();
        builder.setRawData(rawData);
        identifyAppData = builder.getIdentifyData();
    }

    private String getRawData() throws IOException {
        File file = Util.getFile("/fixtures/analyze_post/v2/identify-app.txt");
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
        var contextData = identifyAppData.getContext();
        var userDevice = contextData.getDevice();
        var userScreen = contextData.getScreen();
        var userApp = contextData.getApp();


        // Check Remaining Context Data
        assertEquals("157.51.166.136", contextData.getIp());
        assertEquals("6106", contextData.getAccountId());
        assertEquals("15", contextData.getSrcId());


//         Check AppData
        assertEquals("Trackpad", userApp.getName());
        assertEquals("1.4.0", userApp.getVersion());
        assertEquals("9", userApp.getBuild());


    }

    @Test
    @DisplayName("Validate CustomerProperties Data")
    void validateCustomerPropData() {
        Map<CharSequence,Object> customerProperties = identifyAppData.getCustomerProperties();
        assertEquals("BO13997" , customerProperties.get("user_id"));
        assertEquals("IN" , customerProperties.get("user_country"));
        assertEquals("15" , customerProperties.get("srcid"));
        assertEquals("BO${data.id}" , customerProperties.get("event_name"));
        assertEquals("Chennai" , customerProperties.get("user_city"));
        assertEquals("android" , customerProperties.get("platform"));
        assertEquals("Wed Mar 23 2022 00:36:50 GMT+0530 (IST)" , customerProperties.get("timestamp"));
        assertEquals("anbarasu.m@livspace.com" , customerProperties.get("user_email"));
        assertEquals("identify" , customerProperties.get("event_type"));

    }

    @Test
    @DisplayName("Validate Remaining Data")
    void validateRemainingData() {

        assertEquals("d7b04f22-8d42-448b-bbb4-3486fea16042", identifyAppData.getMessageId());
        assertEquals("", identifyAppData.getReceivedAt());
        assertEquals("Wed Mar 23 2022 00:36:50 GMT+0530 (IST)", identifyAppData.getTimestamp());
        assertEquals("", identifyAppData.getSentAt());
        assertEquals("", identifyAppData.getOriginalTimestamp());
        assertEquals("1647976010775", identifyAppData.getServerTs());

    }


}