package co.lemnisk.transform.analyzepost.classifier;

import co.lemnisk.common.constants.AnalyzePostDataFormat;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class L1AnalyzePostClassifier {

    static final Logger logger = LoggerFactory.getLogger(L1AnalyzePostClassifier.class);

    public static String getDataType(String rawData) {
       LinkedTreeMap<String, String> parsedData = (LinkedTreeMap<String, String>) new Gson().fromJson(rawData, Object.class);

        if(isV3TypeData(parsedData)) {
            return AnalyzePostDataFormat.V3;
        }
        else if (isV2TypeData(parsedData)) {
            return AnalyzePostDataFormat.V2;
        }
        else if (isV1TypeData(parsedData)) {
            return AnalyzePostDataFormat.V1;
        }
        else {
            logger.warn("Got invalid Payload Type for analyze-post topic. Payload: {}", rawData);

            return AnalyzePostDataFormat.UNKNOWN;
        }
    }

    private static boolean isV3TypeData(LinkedTreeMap<String, String> parsedData) {
        return (AnalyzePostDataFormat.V3).equals(payloadType(parsedData));
    }

    private static boolean isV2TypeData(LinkedTreeMap<String, String> parsedData) {
        return (AnalyzePostDataFormat.V2).equals(payloadType(parsedData));
    }

    private static boolean isV1TypeData(LinkedTreeMap<String, String> parsedData) {
        return (AnalyzePostDataFormat.V1).equals(payloadType(parsedData));
    }

    private static String payloadType(LinkedTreeMap<String, String> parsedData) {
        return parsedData.get("payloadType");
    }
}
