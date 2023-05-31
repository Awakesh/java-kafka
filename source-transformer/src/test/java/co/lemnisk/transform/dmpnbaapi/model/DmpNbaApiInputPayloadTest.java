package co.lemnisk.transform.dmpnbaapi.model;

import co.lemnisk.common.Util;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DmpNbaApiInputPayloadTest {

    String payload;

    @BeforeAll
    void constructData() throws IOException {
        String data = getRawData();
        String rawData = "";
        DmpNbaApiInputPayload dmpNbaApiInputPayload = new DmpNbaApiInputPayload();
        dmpNbaApiInputPayload.process(data);
        payload = dmpNbaApiInputPayload.getData();

    }

    private String getRawData() throws IOException {
        File file = Util.getFile("/fixtures/dmpNbaApi/model/track-data.txt");
        return Util.readFileAsString(file);
    }

    @Test
    void process() {

        assertEquals("{\"data\":{\"srcid\":\"2\",\"eventname\":\"launchpad-backend_project:status_changed\",\"type\":\"track\",\"userid\":\"CX1187896\",\"bouncerId\":\"BO5652\",\"properties\":{\"projectId\":\"1143301\",\"customerId\":\"1187896\",\"designerId\":\"11803\",\"statusUpdateReason1\":\"oh_postponed_by_customer\",\"statusUpdateReason2\":null,\"newStatus\":\"ONHOLD\",\"oldStatus\":\"ACTIVE\",\"updatedAt\":\"2022-03-15T03:29:17+00:00\",\"sourceSystem\":\"EAGLE\"},\"id\":\"50fa7dd3-7f6a-465d-b242-cb863513bd9d\",\"external_id\":\"260310e7d4a52ab377dce1959a04b88858fcdeaa6f5c7efe8da5571b5456eaa4\",\"eventtime\":1647314959,\"hashed_email\":\"2ea03ad37d98d1986efceb70e1301f89f866149b464d34e043680344b7175f98\"},\"campaignId\":\"VIZVRM6022\",\"encryptedKey\":\"AQIDAHh4gHMXbIbj7NjE+fVNMJ31tXyRSByI/KF+k5SDk+m8OAHLV7ZvPlazCCftwAnzaTrgAAAAfjB8BgkqhkiG9w0BBwagbzBtAgEAMGgGCSqGSIb3DQEHATAeBglghkgBZQMEAS4wEQQM+3RQigrbq1Ca79JFAgEQgDuDcjK4trxiz76ErEwauJsebTd7t37iMw8egxol1BYn5H6Nu5iZikcUm6y9FEWLraf/d7cn1VTXbMjs1Q==\",\"ds\":\"senderfeedback\"}", payload);

    }
}