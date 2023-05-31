package co.lemnisk.consumer.util;

import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.common.exception.TransformerExceptionHandler;
import co.lemnisk.common.logging.EventLogger;
import co.lemnisk.common.metrics.EventMonitoring;
import co.lemnisk.common.model.CDPDestinationInstance;
import co.lemnisk.common.service.DestinationInstanceMappingService;
import co.lemnisk.common.tracing.TraceHelper;
import co.lemnisk.common.tracing.Tracing;
import co.lemnisk.common.tracing.TracingConstant;
import co.lemnisk.consumer.dto.MessageDTO;
import co.lemnisk.consumer.entity.CDPDestinationApiDetails;
import co.lemnisk.consumer.exception.GAValidationException;
import co.lemnisk.consumer.logger.APIDetailsLogger;
import co.lemnisk.consumer.logger.DestinationSender;
import co.lemnisk.consumer.service.CDPDestinationApiDetailsService;
import co.lemnisk.consumer.utils.ParserUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentelemetry.api.trace.Span;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static co.lemnisk.consumer.util.Constants.DESTINATIONS_WITH_NON_CRITICAL_HEADERS;

@Component
public class
RestClientUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestClientUtil.class);
    public static final String TRANSFORMED_PATH = "$.transformedPayload";
    public static final String DESTINATION_INSTANCE_ID = "$.destinationInstanceId";
    public static final String SOURCE = "source";

    // @Value("${ApiHttpsConnectionCount}")
    private String https_connection_count = "8";

    //@Value("${ApiHttpsConnectionKeepAliveSeconds}")
    private Long https_connection_keep_alive_seconds = 6000L;

    //@Value("${ApiHttpsConnectionReadTimeoutSeconds}")
    private Long https_read_timeout = 120L;

    // @Value("${ApiHttpsConnectionWriteTimeoutSeconds}")
    private Long https_write_timeout = 120L;

    //@Value("${ApiHttpsConnectTimeoutSeconds}")
    private Long https_connect_timeout = 120L;

    @Autowired
    CDPDestinationApiDetailsService cdpDestinationApiDetailsService;

    @Autowired
    DestinationInstanceMappingService destinationInstanceService;

    @Autowired
    EventMonitoring eventMonitoring;

    @Autowired
    ValidationGA validationGA;

    @Autowired
    TransformerExceptionHandler transformerExceptionHandler;

    @Autowired
    Tracing tracing;

    public void process(MessageDTO messageDTO, String transformedPayload, CDPDestinationInstance cdpDestinationInstance ){
        Map<String, String> valuesMap = ParserUtils.getValues(messageDTO.getMessage());
        try {

            CDPDestinationApiDetails cdpDestinationApiDetails = cdpDestinationApiDetailsService.getCDPDestinationApiDetails(cdpDestinationInstance.getCdpDestinationId());

            if (Objects.isNull(cdpDestinationApiDetails)) {
                // TODO: Check how this exception is caught
                throw new TransformerException("No data for destinationId: " + cdpDestinationInstance.getCdpDestinationId() + " is found in the CDPDestinationApiDetails table");
            }

            switch (cdpDestinationApiDetails.getRequestType()) {
                case Constants.METHOD_POST:
                    LOGGER.info("Post method called.");
                    postType(messageDTO, cdpDestinationApiDetails, valuesMap, transformedPayload, cdpDestinationInstance);
                    break;
                case Constants.METHOD_GET:
                    LOGGER.info("Get method called.");
                    getType(messageDTO, cdpDestinationApiDetails, valuesMap, transformedPayload, cdpDestinationInstance);
                    break;
                default:
                    throw new TransformerException("Invalid Request Type");
            }
        }
        catch (TransformerException te) {
            Span span = TraceHelper.createSpan(tracing, messageDTO.getHeaders(), TracingConstant.SpanNames.DESTINATION_SENDER, messageDTO.getTopicName());
            TransformerException transformerException = new TransformerException(
                    te.getMessage(),
                    StringUtils.isNotEmpty(te.getTopic()) ? te.getTopic() : messageDTO.getTopicName(),
                    StringUtils.isNotEmpty(te.getKafkaPayload()) ? te.getKafkaPayload(): messageDTO.getMessage(),
                    Objects.nonNull(te.getDropPayload()) && te.getDropPayload());
            transformerExceptionHandler.handle(transformerException, span);
        }
        catch (Exception e){
            Span span = TraceHelper.createSpan(tracing, messageDTO.getHeaders(), TracingConstant.SpanNames.DESTINATION_SENDER, messageDTO.getTopicName());
            TransformerException transformerException = new TransformerException(e.getMessage(), messageDTO.getTopicName(), messageDTO.getMessage(), false);
            transformerExceptionHandler.handle(transformerException, span);
            LOGGER.error("Exception in processing", e);
        }
    }
    public void postType(MessageDTO messageDTO, CDPDestinationApiDetails cdpDestinationApiDetails, Map<String, String> valuesMap, String transformedPayload, CDPDestinationInstance cdpDestinationInstance) {

        Map<String, String> headerMap = new HashMap<>();
        Map<String, Object> queryParameterMap = new HashMap<>();

        try {
            // Adding QP from DB
            String dbQueryParams = cdpDestinationApiDetails.getQueryParameter();
            if(StringUtils.isNotEmpty(dbQueryParams)) {
                String parsedQP = MacroParser.parse(cdpDestinationInstance.getConfigJson(), dbQueryParams);
                JSONObject dbQPJson = new JSONObject(parsedQP);
//                for (String key : dbQPJson.keySet()) {
//                    String validStr = validateAndParseQPValue(dbQPJson.get(key));
//                    if (StringUtils.isNotEmpty(validStr)) {
//                        queryParameterMap.put(key, validStr);
//                    }
//                }
            }
        }
        catch (Exception e){
            LOGGER.error("Exception while parsing the QueryParameters from database {}", e);
            throw new TransformerException(e.getMessage(), messageDTO.getTopicName(), messageDTO.getMessage(), false);
        }

        try{
            // Headers from DB
            String parsedHeader = MacroParser.parse(cdpDestinationInstance.getConfigJson(), cdpDestinationApiDetails.getHeader());
            JSONObject dbHeaderJson = new JSONObject(parsedHeader);
//            for (String key : dbHeaderJson.keySet()) {
//                String validStr = validateAndParseHeaderValue(key, dbHeaderJson.get(key), cdpDestinationInstance.getCdpDestinationId());
//                if (StringUtils.isNotEmpty(validStr)) {
//                    headerMap.put(key, validStr);
//                }
//            }
        } catch (Exception e){
            LOGGER.error("Exception while parsing the Headers from database {}", e);
            throw new TransformerException(e.getMessage(), messageDTO.getTopicName(), messageDTO.getMessage(), false);
        }

        try{
            // Unescape payload to proper json
            ObjectMapper mapper = new ObjectMapper();
            transformedPayload = mapper.readValue(transformedPayload, String.class);
            callApiPost(cdpDestinationApiDetails.getEndpoint(),transformedPayload,headerMap,valuesMap,messageDTO);
        } catch (Exception ex){
            LOGGER.error("Exception while sending message to rest endpoint: {}", ex);
            throw new TransformerException(ex.getMessage(), messageDTO.getTopicName(), messageDTO.getMessage(), false);
        }
    }

    public void getType(MessageDTO messageDTO, CDPDestinationApiDetails cdpDestinationApiDetails, Map<String, String> valuesMap, String transformedPayload, CDPDestinationInstance cdpDestinationInstance) {

        Map<String, String> headerMap = new HashMap<>();
        Map<String, String> queryParameterMap = new HashMap<>();

        try {
//          Adding QP from DB
            String parsedQP = MacroParser.parse(cdpDestinationInstance.getConfigJson(), cdpDestinationApiDetails.getQueryParameter());
            JSONObject dbQPJson = new JSONObject(parsedQP);
//            for (String key : dbQPJson.keySet()) {
//                String validStr = validateAndParseQPValue(dbQPJson.get(key));
//                if (StringUtils.isNotEmpty(validStr)) {
//                    queryParameterMap.put(key, validStr);
//                }
//            }
        }
        catch (Exception e){
            LOGGER.error("Exception while adding params from Query Parameters {}", e);
            throw new TransformerException(e.getMessage(), messageDTO.getTopicName(), messageDTO.getMessage(), false);
        }

        try{
            // Adding QP from Payload
            transformedPayload = transformedPayload.replaceAll("(\\r\\n|\\n|\\r|\\t)", "");
            transformedPayload = transformedPayload.replaceAll("\\\\", "");
            transformedPayload = removeFirstAndLast(transformedPayload);
            String payloadQP = JsonUtils.parseJson(transformedPayload, "$.qp");
            queryParameterMap.putAll(JsonToMapMapper.toStringMap(payloadQP));
        } catch (Exception e){
            LOGGER.error("Exception while parsing (transformedPayload) and creating the params for GET: {}",e);
            throw new TransformerException(e.getMessage(), messageDTO.getTopicName(), messageDTO.getMessage(), false);
        }

        try{
//          Headers from DB
            String parsedHeader = MacroParser.parse(cdpDestinationInstance.getConfigJson(), cdpDestinationApiDetails.getHeader());
            JSONObject dbHeaderJson = new JSONObject(parsedHeader);
//            for (String key : dbHeaderJson.keySet()) {
//                String validStr = validateAndParseHeaderValue(key, dbHeaderJson.get(key), cdpDestinationInstance.getCdpDestinationId());
//                if (StringUtils.isNotEmpty(validStr)) {
//                    headerMap.put(key, validStr);
//                }
//            }

//          Headers from Payload
            Map<String, Object> payloadHeadersMap = JsonToMapMapper.toMap(JsonUtils.parseJson(transformedPayload, "$.headers"));
            if (Objects.nonNull(payloadHeadersMap)) {
                for (Map.Entry<String, Object> payloadHeaderEntry : payloadHeadersMap.entrySet()) {
                    headerMap.put(payloadHeaderEntry.getKey(), String.valueOf(payloadHeaderEntry.getValue()));
                }
            }

        } catch (Exception e){
            LOGGER.error("Exception while adding params to builder and Creating the Header {}",e);
            throw new TransformerException(e.getMessage(), messageDTO.getTopicName(), messageDTO.getMessage(), false);
        }

        try {
            validationGA.validateTransformedPayload(headerMap, queryParameterMap, messageDTO, cdpDestinationInstance.getCdpDestinationId());
        }
        catch (GAValidationException gaEx) {
            throw new TransformerException(gaEx.getMessage(), gaEx.getTopic(), gaEx.getKafkaPayload(), gaEx.getDropPayload());
        }
        catch (Exception e) {
            throw new TransformerException(e.getMessage(), messageDTO.getTopicName(), messageDTO.getMessage(), false);
        }

        try{
            callApiGet(cdpDestinationApiDetails.getEndpoint(),queryParameterMap, headerMap, valuesMap, messageDTO);
        } catch (Exception e){
            LOGGER.error("Exception while sending message to rest endpoint: {}", e);
            throw new TransformerException(e.getMessage(), messageDTO.getTopicName(), messageDTO.getMessage(), false);
        }
    }

    private String validateAndParseQPValue(Object value) {
        String macro = (String) value;
        if(isMacroString(macro)) {
            return null;
        }
        else {
            return macro;
        }
    }

    private String validateAndParseHeaderValue(String key, Object value, Integer destinationId) {
        String macro = (String) value;
        if(isMacroString(macro)) {
            if(DESTINATIONS_WITH_NON_CRITICAL_HEADERS.contains(destinationId)) {
                return null;
            }
            else {
                throw new TransformerException("Unable to find macro value for key '"
                        + key +
                        "' in the configJson column of CDPDestinationInstance table for DestinationId: "
                        + destinationId);
            }
        }
        else {
            return macro;
        }
    }

    private static boolean isMacroString(String str) {
        Pattern pattern = Pattern.compile(Constants.MACROS_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    public String removeFirstAndLast(String str){
        str = str.substring(1, str.length() - 1);
        return str;
    }


    private void callApiPost(String url, String body , Map<String,String> headers, Map<String, String> valuesMap, MessageDTO messageDTO ) {

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        try {

            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }

            StringEntity bodyString = new StringEntity(body);
            httpPost.setEntity(bodyString);
            httpPost.setHeader("Content-type", "application/json");

            CloseableHttpResponse response = client.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();
            String accountId = valuesMap.get("accountId");

            DestinationSender.getInstance().log(url, EventLogger.MESSAGE_FLOW.OUTGOING,body + "| Code: " + statusCode);

            if (statusCode == 200) {
                eventMonitoring.addDestResponseMetrics(valuesMap.get("srcId"), accountId, valuesMap.get("destinationInstanceId"), String.valueOf(statusCode));
            }
            else {
                LOGGER.error("Exception in sending data to endpoint {} Code {}  Response {} ", url, statusCode, response.getEntity());
            }

            APIDetailsLogger.logPostApi(body, url, messageDTO.getMessage(), valuesMap.get("messageId"), accountId, messageDTO.getTopicName(), response);

            response.close();
            client.close();

        } catch (Exception e) {
            LOGGER.error("Error in reading response for connection: {}", e.toString());
            throw new TransformerException(e.getMessage(), messageDTO.getTopicName(), messageDTO.getMessage(), false);
        }
    }

    private void callApiGet(String url, Map<String, String> queryParameterMap, Map<String, String> headers, Map<String, String> valuesMap, MessageDTO messageDTO) {

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        URIBuilder builder = new URIBuilder(httpGet.getURI());

        for (Map.Entry<String, String> entry : queryParameterMap.entrySet()) {
            builder.setParameter(entry.getKey(), entry.getValue());
        }

        try {
            URI uri = builder.build();
            httpGet.setURI(uri);

            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }

            httpGet.setHeader("Content-type", MediaType.APPLICATION_JSON_VALUE);

            CloseableHttpResponse response = client.execute(httpGet);

            int statusCode = response.getStatusLine().getStatusCode();
            String accountId = valuesMap.get("accountId");

            DestinationSender.getInstance().log(url, EventLogger.MESSAGE_FLOW.OUTGOING, "| Code: " + statusCode);

            if (HttpStatus.valueOf(statusCode).is2xxSuccessful()) {
                eventMonitoring.addDestResponseMetrics(valuesMap.get("srcId"), accountId, valuesMap.get("destinationInstanceId"), String.valueOf(statusCode));
            }
            else {
                LOGGER.error("Exception in sending data to endpoint {} Code {}  Response {} ", url, statusCode, response.getEntity());
            }

            APIDetailsLogger.logGetApi(url, queryParameterMap.toString(), messageDTO.getMessage(), valuesMap.get("messageId"), accountId, messageDTO.getTopicName(), response);

            response.close();
            client.close();

        } catch (Exception e) {
            LOGGER.error("Error in reading response for connection: {}", e.toString());
            throw new TransformerException(e.getMessage(), messageDTO.getTopicName(), messageDTO.getMessage(), false);
        }

    }

}