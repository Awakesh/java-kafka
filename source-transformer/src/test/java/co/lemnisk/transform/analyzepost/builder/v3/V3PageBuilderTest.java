package co.lemnisk.transform.analyzepost.builder.v3;
import co.lemnisk.common.Util;
import co.lemnisk.common.avro.model.event.page.Context;
import co.lemnisk.common.avro.model.event.page.ContextLibrary;
import co.lemnisk.common.avro.model.event.page.ContextUserAgent;
import co.lemnisk.common.avro.model.event.page.Page;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class V3PageBuilderTest {
  // private V3PageBuilder builder;
   private Page pageData;


   @BeforeAll
   void constructData() throws IOException {
     String rawData = getRawData();
       V3PageBuilder builder = new V3PageBuilder();
     builder.setRawData(rawData);
     pageData =builder.build();
   }

   private String getRawData() throws IOException {
      File file = Util.getFile("/fixtures/analyze_post/v3/page.txt");
      return Util.readFileAsString(file);
   }

   @Test
    @DisplayName("Validate Context Data")
    void validateContextData() {
       Context context =pageData.getContext();
       ContextLibrary library=context.getLibrary();
       ContextUserAgent userAgent=context.getUserAgent();

       // Check LibraryData

       assertEquals("javascript", library.getName());
       assertEquals("0.01", library.getVersion());

       // Check UserAgentData

       assertEquals("Android", userAgent.getOsType());
       assertEquals("10", userAgent.getOsVersion());

       //check Remaining Data

       assertEquals("112.201.128.29",context.getIp());
       assertEquals("6037", context.getAccountId());
       assertEquals("203", context.getSrcId());

   }

    @Test
    @DisplayName("Validate OtherIds")
    void validateOtherIds(){
        Map<CharSequence, Object> customerOtherIds =  pageData.getOtherIds();
        assertEquals("fb.1.1640619377234.61954491",customerOtherIds.get("fbp"));
        assertEquals("fb.1.1640619377230.IwAR1qODDffQN6ZruCmnh1ZVkgo2m0x_EoYcL0SiAmHCxp5Xg4f0K7PXBreMM",customerOtherIds.get("fbc"));
        assertEquals("GA1.1.255178833.1640619377",customerOtherIds.get("ga"));
    }

    @Test
    @DisplayName("Validate  Properties Data")
    void validatePropertiesData() {
        Map<CharSequence, Object> propertiesData = pageData.getProperties();
        assertEquals("Home",propertiesData.get("path"));
        assertEquals("https://l.facebook.com/",propertiesData.get("referrer"));
        assertEquals("Home",propertiesData.get("name"));
        assertEquals("Canvas Of You",propertiesData.get("title"));
        assertEquals("https://www.canvasofyou.com/Home",propertiesData.get("url"));
    }

    @Test
    @DisplayName("Validate Remaining Data")
    void validateRemainingData() {
        assertEquals("3c997918-ed85-4683-8b56-2a5f9d9470c4",pageData.getMessageId());
        assertEquals("viz_61c9dd791ad84",pageData.getId());
        assertEquals("page",pageData.getType());
        assertEquals("1640619415428",pageData.getReceivedAt());
        assertEquals("1640619415390",pageData.getOriginalTimestamp());
        assertEquals("1640619415428",pageData.getServerTs());
        assertEquals("tjwcn0ru0kov22mm46yc",pageData.getWriteKey());

    }
}