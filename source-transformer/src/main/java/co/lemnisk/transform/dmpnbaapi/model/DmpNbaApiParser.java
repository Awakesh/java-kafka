package co.lemnisk.transform.dmpnbaapi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class DmpNbaApiParser {

    @Setter
    private Data data = new Data();

    @Setter
    private String campaignId = "";

    @Getter
    public static class Data {
        private String userid = "";
        private Map<CharSequence, Object> properties = new LinkedHashMap<>();
        private String id = "";
        private String type = "";
        private String eventname = "";
        private Map<CharSequence, Object> otherIds = new LinkedHashMap<>();
        private String srcid = "";
        private String bouncerId = "";
        private String external_id = "";
        private String eventtime = "";
        private String hashed_email = "";
        private String hashed_mobile = "";
        private String WriteKey = "";
    }

}
