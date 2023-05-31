package co.lemnisk.transform.analyzepost.model.parser.v2;

import co.lemnisk.common.constants.EventType;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@lombok.Data
public class V2DataParser {
        private String event_type = "";
        private Map<CharSequence, Object> prop = new LinkedHashMap<>();
        private Map<CharSequence, Object> otherIds = new LinkedHashMap<>();
        private String input_event_name = "";
        private String receivedAt = "";
        private Context context ;

        @Getter
        public static class Context {
                private String sdk_build_name = "";
                private String server_ts = "";
                private String ios_version = "";
                private String ios_device = "";
                private String timezone = "";
                private String msgId = "";
                private String event_source = "";
                private String sdk_build_env = "";
                private String ua = "";
                private String sdk_build_number = "";
                private String deviceId = "";
                private String lemid = "";
                private String param = "";
                private String i_dt = "";
                private String adv_id = "";
                private String device_pref_lang = "";
                private String notif_state = "";
                private String tas = "";
                private String ip = "";
                private String sentAt = "";
                private String u_dt = "";
                private String sdk_build_version = "";
                private String app_namespace = "";
                private String token = "";
                private String device_ets = "";
                private String app_name = "";
                private String event_sdk = "";
                private String device_lang = "";
                private String account_id = "";
                private String app_build = "";
                private String push_enabled = "";
                private String app_ver = "";
                private String csm = "";
                private String ifaFlagEnabled = "";
                private String density_dpi = "";
                private String hostname = "";
                private String event_type = "";
                private String host = "";
                private String is_lat = "";
                private String vizid = "";
                private String stage = "";
                private String srcid = "";
                private String server_dt = "";
        }

        public String eventType() {
                if (getProp().get("event_type") == null){
                        return String.valueOf(getProp().get("eventCategory"));
                }
               return String.valueOf(getProp().get("event_type"));
        }

        public String getEventName() {
                if (EventType.IDENTIFY.equals(eventType())){
                        return EventType.IDENTIFY;
                }
                if (EventType.SCREEN.equals(eventType())){
                        return EventType.SCREEN;
                }
                if (EventType.TRACK.equals(eventType())){
                        if (getProp().get("event_name") != null){
                                return String.valueOf(getProp().get("event_name"));
                        }
                        else if (getInput_event_name() != null){
                                return getInput_event_name();
                        }
                        else {
                                return "";
                        }
                }

                return EventType.UNKNOWN;
        }

}
