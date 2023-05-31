package co.lemnisk.consumer.logger;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class APIDetailsLogger {

    static final Logger logger = LoggerFactory.getLogger(APIDetailsLogger.class.getName());

    enum RequestType {
        GET,
        POST
    }

    public static void logPostApi(String body, String url, String payload, String messageId, String campaignId, String topicName, CloseableHttpResponse response) throws JSONException {

        StatusLine sl = response.getStatusLine();
        HttpEntity entity = response.getEntity();
        String rm = "";

        LinkedTreeMap<String, String> parsedData = (LinkedTreeMap<String, String>) new Gson().fromJson(payload, Object.class);

        try { rm = EntityUtils.toString(entity, StandardCharsets.UTF_8); } catch (IOException ignored) {}

        JSONObject detailsJson = new JSONObject();
        detailsJson.put("rqt", RequestType.POST.name());
        detailsJson.put("url", url);
        detailsJson.put("rb", body);
        detailsJson.put("rc", sl.getStatusCode());
        detailsJson.put("cid", campaignId);
        detailsJson.put("inputEvent", parsedData);
        detailsJson.put("rm", rm);
        detailsJson.put("topic", topicName);
        detailsJson.put("messageId", messageId);

        logger.info(detailsJson.toString());
    }

    public static void logGetApi(String url, String rqp, String payload, String messageId, String campaignId, String topicName, CloseableHttpResponse response) {

        StatusLine sl = response.getStatusLine();
        HttpEntity entity = response.getEntity();
        String rm = "";

        LinkedTreeMap<String, String> parsedData = (LinkedTreeMap<String, String>) new Gson().fromJson(payload, Object.class);

        try { rm = EntityUtils.toString(entity, StandardCharsets.UTF_8); } catch (IOException ignored) {}

        JSONObject detailsJson = new JSONObject();
        detailsJson.put("rqt", RequestType.GET.name());
        detailsJson.put("url", url);
        detailsJson.put("rqp", rqp);
        detailsJson.put("rc", sl.getStatusCode());
        detailsJson.put("cid", campaignId);
        detailsJson.put("inputEvent", parsedData);
        detailsJson.put("rm", rm);
        detailsJson.put("topic", topicName);
        detailsJson.put("messageId", messageId);

        logger.info(detailsJson.toString());
    }
}
