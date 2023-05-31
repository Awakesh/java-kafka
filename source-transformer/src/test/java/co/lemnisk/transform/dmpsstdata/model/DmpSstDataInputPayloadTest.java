package co.lemnisk.transform.dmpsstdata.model;

import co.lemnisk.common.Util;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DmpSstDataInputPayloadTest {

    HashMap payloadData;


    @BeforeAll
    void constructData() throws IOException {
        String data = getRawData();
        String rawData = "";
        DmpSstDataInputPayload dmpSstDataInputPayload = new DmpSstDataInputPayload();
        dmpSstDataInputPayload.process(rawData, data);
        payloadData = dmpSstDataInputPayload.getData();

    }

    private String getRawData() throws IOException {
        File file = Util.getFile("/fixtures/dmpsstdata/model/inputPayloadData.txt");
        return Util.readFileAsString(file);
    }

    @Test
    void process() {
        assertEquals("223.189.64.38", payloadData.get("ip"));
        assertEquals("VIZVRM5459", payloadData.get("account_id"));
        assertEquals("1647940949265", payloadData.get("server_ts"));
    }

}
