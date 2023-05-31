package co.lemnisk.consumer.util;
import co.lemnisk.common.exception.TransformerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JsonOrgJsonProvider;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class JsonUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, String> convert(final String json) {
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (IOException e) {
            LOGGER.error("Error in converting json to Map {}", e);
        }
        return null;
    }

    public static JSONObject updateJson(JSONObject obj, String keyString, String newValue) throws Exception {
        JSONObject json = new JSONObject();
        // get the keys of json object
        Iterator iterator = obj.keys();
        String key = null;
        while (iterator.hasNext()) {
            key = (String) iterator.next();
            // if the key is a string, then update the value
            if ((obj.optJSONArray(key) == null) && (obj.optJSONObject(key) == null)) {
                if ((key.equals(keyString))) {
                    // put new value
                    obj.put(key, newValue);
                    return obj;
                }
            }

            // if it's jsonobject
            if (obj.optJSONObject(key) != null) {
                updateJson(obj.getJSONObject(key), keyString, newValue);
            }

            // if it's jsonarray
            if (obj.optJSONArray(key) != null) {
                JSONArray jArray = obj.getJSONArray(key);
                for (int i = 0; i < jArray.length(); i++) {
                    updateJson(jArray.getJSONObject(i), keyString, newValue);
                }
            }
        }
        return obj;
    }

    public static String parseJson(String jsonData, String path) {
        String json = "";
        try {
            Configuration configuration = Configuration.builder().jsonProvider(new JsonOrgJsonProvider()).build();
            JsonPath jsonPath = JsonPath.compile(path);
            Object transformedPayload= jsonPath.read(jsonData.toString(), configuration);
            json = transformedPayload.toString();
        }catch (Exception e) {
            e.printStackTrace();
             throw new TransformerException("Error parsing json data" + jsonData );
        }
        return json;
    }

    public static String getPayload(String jsonData, String path) {
        String json = "";
        try {
            Configuration configuration = Configuration.builder().jsonProvider(new JsonOrgJsonProvider()).build();
            JsonPath jsonPath = JsonPath.compile(path);
            Object transformedPayload= jsonPath.read(jsonData.toString(), configuration);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            json = ow.writeValueAsString(transformedPayload);
        }catch (JsonProcessingException e) {
            e.printStackTrace();
            LOGGER.error("Error in parsing json {} " ,e);
        }
        return json;
    }
}

