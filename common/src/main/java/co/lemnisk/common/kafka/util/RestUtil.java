package co.lemnisk.common.kafka.util;

import java.io.IOException;
import java.util.Properties;

import co.lemnisk.common.Util;
import co.lemnisk.common.constants.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Component
public class RestUtil {

    OkHttpClient client = new OkHttpClient();


    private static Logger LOGGER = LogManager.getLogger(RestUtil.class);

    public String post(String topicName) throws IOException {
        Resource resource = new ClassPathResource("/application-common.properties");
        Properties props = PropertiesLoaderUtils.loadProperties(resource);

        StringBuilder sb = new StringBuilder();
        sb.append(props.getProperty("common.destination.sender.url-prefix"));
        sb.append(Constants.URI_CREATE);
        sb.append("?topicName=");
        sb.append(topicName);

        Request request = new Request.Builder().url(sb.toString()).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public String postRequest(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public String getUrl() throws IOException {
        Resource resource = new ClassPathResource("/application-common.properties");
        Properties props = PropertiesLoaderUtils.loadProperties(resource);

        StringBuilder sb = new StringBuilder();
        sb.append(props.getProperty("common.destination.sender.url-prefix"));
        sb.append(Constants.GET_CONSUMER_IDS);
        return sb.toString();
    }
}