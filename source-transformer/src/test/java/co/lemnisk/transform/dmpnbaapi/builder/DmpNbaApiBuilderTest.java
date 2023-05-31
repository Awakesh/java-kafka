package co.lemnisk.transform.dmpnbaapi.builder;

import co.lemnisk.common.Util;
import co.lemnisk.common.avro.model.event.dmpnba.DmpNba;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DmpNbaApiBuilderTest {

    private DmpNba dmpNbaData;

    @BeforeAll
    void constructData() throws IOException {
        String data = getRawData();
        String rawData = "";
//        DmpNbaApiBuilder builder = new DmpNbaApiBuilder(data, rawData);
//        dmpNbaData = builder.buildEventData();

    }

    private String getRawData() throws IOException {
        File file = Util.getFile("/fixtures/dmpNbaApi/builder/identify-data.txt");
        return Util.readFileAsString(file);
    }

    @Test
    @DisplayName("Validate Context Data")
    void validateContextData() {
        var contextData = dmpNbaData.getContext();

        //checking context data

        assertEquals("117", contextData.getSrcId());
        assertEquals("VIZVRM6022", contextData.getAccountId());
        assertEquals("", contextData.getWriteKey());
    }

    @Test
    @DisplayName("Validate Remaining Data")
    void validateRemainingData() {

        assertEquals("1972fbc6-6395-478c-a3a2-bbaeac1f06fb", dmpNbaData.getMessageId());
        assertEquals("", dmpNbaData.getUserId());
        assertEquals("track", dmpNbaData.getType());

    }

}