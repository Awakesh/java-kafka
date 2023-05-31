package co.lemnisk.consumer.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.JsonPathException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ParserUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParserUtils.class);

    public static Map<String, String> getValues(String json){
        Map<String, String> valuesMap = new HashMap<>();
        try {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            JsonElement element = gson.fromJson (json, JsonElement.class);
            JsonObject jsonObj = element.getAsJsonObject();
            valuesMap.put("destinationInstanceId", jsonObj.get("destinationInstanceId").getAsString());
            valuesMap.put("type", jsonObj.get("type").getAsString());
            valuesMap.put("userId", jsonObj.get("userId").getAsString());
            valuesMap.put("messageId", jsonObj.get("messageId").getAsString());
            JsonObject context = jsonObj.getAsJsonObject("context");
            valuesMap.put("srcId", context.get("srcId").getAsString());
            valuesMap.put("accountId", context.get("accountId").getAsString());
            JsonObject properties = jsonObj.getAsJsonObject("properties");
            if (Objects.nonNull(properties)) {
                if (Objects.nonNull(properties.get("source"))) {
                    valuesMap.put("source", properties.get("source").getAsString());
                }
            }
        }catch (JsonPathException jpe){
            LOGGER.error("Exception in processing json {}", jpe);
        }
        return valuesMap;
    }
    public static boolean hasKey(String json, String key){
         boolean has = false;
        try {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            JsonElement element = gson.fromJson (json, JsonElement.class);
            JsonObject jsonObj = element.getAsJsonObject();
            has = jsonObj.has(key);
        }catch (JsonPathException jpe){
            LOGGER.error("Exception in processing json {}", jpe);
            has = false;
        }
        return has;
    }
}


