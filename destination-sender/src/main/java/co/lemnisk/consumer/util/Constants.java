package co.lemnisk.consumer.util;

import scala.Int;

import java.util.Arrays;
import java.util.List;

public class Constants {
	public static final String DEFAULT_LISTENER = "KafkaCustomMessageListener";
	public static final String V = "v";
	public static final String V_VAL = "1";
	public static final String TID = "tid";
	public static final String TID_VAL = "UA-26443900-1";
	public static final String CID = "cid";
	public static final String T = "t";
	public static final String T_PAGE_VIEW = "pageview";
	public static final String DP = "dp";
	public static final String DP_VAL = "%2Fhome%2Famp";
	public static final String USER_AGENT = "User-Agent";
	public static final String SOURCE = "source";
	public static final String METHOD_POST = "POST";
	public static final String METHOD_GET = "GET";
	public static final String API_HEADERS = "h";
	public static final String USER_ID = "userId";
	public static final Integer GA_DESTINATION_ID = 10;
	public static final Integer S3_DESTINATION_ID = 7;
	public static final String API_EQUALS = "=";
	public static final String API_AMPERSAND = "&";

	//GA Exception messages
	public static final String KEY_V_NOT_AVAILABLE = "Key v is not available";
	public static final String KEY_T_NOT_AVAILABLE = "Key t is not available";
	public static final String KEY_TID_NOT_AVAILABLE = "Key tid is not available";
	public static final String KEY_DP_NOT_AVAILABLE = "Key dp is not available for 'pageview' hit-type";

	public static final String KEY_CID_NOT_AVAILABLE = "Key cid is not available";

	public static final String KEY_QP = "Key qp is not available in transformedPayload.";
	public static final String KEY_USER_AGENT_NOT_AVAILABLE = "Key User-Agent is not available";
	public static final String KEY_HEADERS = "Key headers is not available in transformedPayload.";

	//GA constants
	public static final String QP = "qp";
	public static final String HEADERS = "headers";

	public static final String DESTINATION_INSTANCE_ID = "destinationInstanceId";

	public static final List<Integer> DESTINATIONS_WITH_NON_CRITICAL_HEADERS = Arrays.asList(GA_DESTINATION_ID);

	public static final String MACROS_REGEX = "\\$\\{(.+?)\\}";

	enum Status{
		CREATED,
		DELETED
	}
}