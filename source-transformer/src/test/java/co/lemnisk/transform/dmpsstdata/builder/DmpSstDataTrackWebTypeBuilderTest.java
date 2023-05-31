package co.lemnisk.transform.dmpsstdata.builder;

import co.lemnisk.common.Util;
import co.lemnisk.common.avro.model.event.track.web.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DmpSstDataTrackWebTypeBuilderTest {

    private DmpSstDataTrackTypeBuilder builder;

    @BeforeAll
    void constructData() throws IOException {

        String filePath = "/fixtures/dmpsstdata/track-web.json";

        HashMap<String, String> mapData = Util.jsonFileToMap(filePath);

        builder = new DmpSstDataTrackTypeBuilder(mapData, "");
        builder.constructData();
    }

    @Test
    @DisplayName("Validate Identify Data")
    void ValidateContextData () {
        TrackWeb data = builder.getData();

        Context context = data.getContext();
        ContextLibrary library = context.getLibrary();
        ContextUserAgent userAgent = context.getUserAgent();
        ContextUtm utm = context.getUtm();

        // Check LibraryData
        assertEquals("smarttag", library.getName());
        assertEquals("0.29v", library.getVersion());

        // Check UserAgent
        assertEquals("MOBILE", userAgent.getDeviceType());
        assertEquals("Android", userAgent.getOsType());
//        assertEquals("Linux", userAgent.getBrowser());
        assertEquals("Linux", userAgent.getOsVersion());
        assertEquals("Mozilla", userAgent.getUa());

        // Check UTM
//        assertEquals("poojaroom", utm.getCampaign());
//        assertEquals("facebook", utm.getSource());
//        assertEquals("post", utm.getMedium());
//        assertEquals("29092021", utm.getTerm());
//        assertEquals("poojaroom", utm.getContent());

        assertEquals("127.0.0.1", context.getIp());
        assertEquals("6081", context.getAccountId());
        assertEquals("7", context.getSrcId());
    }

    @Test
    @DisplayName("Validate OtherIds Data")
    void ValidateOtherIdsData () {
        TrackWeb data = builder.getData();
        Map<CharSequence, Object> otherIds = data.getOtherIds();

        assertEquals("sample-ga-value", otherIds.get("_ga"));
        assertEquals("sample-fbp-value", otherIds.get("_fbp"));
        assertEquals("some-hash-code-for-email", otherIds.get("email"));
        assertEquals("some-hash-code-for-phone", otherIds.get("phone"));
    }

    @Test
    @DisplayName("Validate Page Data")
    void ValidatePageData () {
        TrackWeb data = builder.getData();

        ContextPage page = data.getContext().getPage();

        assertEquals("Livspace Hub", page.getTitle());
        assertEquals("/boq", page.getPath());
        assertEquals("https://hub.livspace.com/project/boqs", page.getReferrer());
        assertEquals("https://hub.livspace.com/boq?id=978578", page.getUrl());
        assertEquals("?id=978578", page.getSearch());
    }

    @Test
    @DisplayName("Validate Property Data")
    void ValidatePropertyData () {
        TrackWeb data = builder.getData();

        Map<CharSequence, Object> properties = data.getProperties();

        assertEquals("374811", properties.get("t_payload_projectID"));
        assertEquals("CX392091", properties.get("t_userid"));
        assertEquals("customer_quote_sections:select_selections_click", properties.get("t_event"));
        assertEquals("customer_quote_sections:select_selections_click", properties.get("t_event_name"));
        assertEquals("392091", properties.get("t_payload_userId"));
    }

    @Test
    @DisplayName("Validate Remaining Data")
    void ValidateRemainingData () {
        TrackWeb data = builder.getData();

        assertEquals("CX392091", data.getUserId());
        assertEquals("8fcfaa7e-ab61-46fa-a576-eb728629c728", data.getMessageId());
        assertEquals("2022-01-16T06:14:53.000Z", data.getSentAt());
        assertEquals("2022-01-16T06:14:53.000Z", data.getOriginalTimestamp());
        assertEquals("viz_61da7fd00753e", data.getId());
        assertEquals(builder.eventType(), data.getType());
//        assertEquals("s6b9tbu0y0b5xxtqtiv4", data.getWriteKey());
    }

}