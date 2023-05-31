package co.lemnisk.consumer.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.confluent.kafka.schemaregistry.utils.JacksonMapper;

import java.util.HashMap;
import java.util.Map;

public class JsonToMapMapper {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final JacksonMapper INSTANCE;
    static {
        INSTANCE = new JacksonMapper();
    }

    private JsonToMapMapper() {
        // not called
    }

    public static JacksonMapper getInstance() {
        return INSTANCE;
    }

    public static Map<String, String> toStringMap(String jsonString) throws Exception {
        return mapper.readValue(jsonString, new TypeReference<HashMap<String, String>>(){});
    }

    public static Map<String, Object> toMap(String jsonString) throws Exception {
        return mapper.readValue(jsonString, new TypeReference<HashMap<String, Object>>(){});
    }
}

