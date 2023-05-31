package co.lemnisk.consumer.util;

import co.lemnisk.consumer.dto.MessageDTO;
import co.lemnisk.consumer.exception.GAValidationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ValidationGA {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationGA.class);

    public void validateTransformedPayload(Map<String, String> headers, Map<String, String> queryParameters, MessageDTO messageDTO, Integer destinationId){

        //validation for Google Analytics GET type
        if(Constants.GA_DESTINATION_ID.equals(destinationId)){

            String topic = messageDTO.getTopicName();

            // QueryParameters Validations
            if(!StringUtils.isNotEmpty(String.valueOf(queryParameters.getOrDefault(Constants.V, "")))){
                LOGGER.error(Constants.KEY_V_NOT_AVAILABLE);
                throw new GAValidationException(Constants.KEY_V_NOT_AVAILABLE, topic, messageDTO.getMessage(), false);
            }

            if(!StringUtils.isNotEmpty(String.valueOf(queryParameters.getOrDefault(Constants.CID, "")))){
                LOGGER.error(Constants.KEY_CID_NOT_AVAILABLE);
                throw new GAValidationException(Constants.KEY_CID_NOT_AVAILABLE, topic, messageDTO.getMessage(), false);
            }

            if(!StringUtils.isNotEmpty(String.valueOf(queryParameters.getOrDefault(Constants.T, "")))){
                LOGGER.error(Constants.KEY_T_NOT_AVAILABLE);
                throw new GAValidationException(Constants.KEY_T_NOT_AVAILABLE, topic, messageDTO.getMessage(), false);
            }

            if(!StringUtils.isNotEmpty(String.valueOf(queryParameters.getOrDefault(Constants.TID, "")))){
                LOGGER.error(Constants.KEY_TID_NOT_AVAILABLE);
                throw new GAValidationException(Constants.KEY_TID_NOT_AVAILABLE, topic, messageDTO.getMessage(), false);
            }

            if(Constants.T_PAGE_VIEW.equals(queryParameters.get(Constants.T)) && !StringUtils.isNotEmpty(String.valueOf(queryParameters.getOrDefault(Constants.DP, "")))){
                LOGGER.error(Constants.KEY_DP_NOT_AVAILABLE);
                throw new GAValidationException(Constants.KEY_DP_NOT_AVAILABLE, topic, messageDTO.getMessage(), false);
            }

            // Headers Validations
            if(!StringUtils.isNotEmpty(headers.get(Constants.USER_AGENT))) {
                LOGGER.error(Constants.KEY_USER_AGENT_NOT_AVAILABLE);
                throw new GAValidationException(Constants.KEY_USER_AGENT_NOT_AVAILABLE, topic, messageDTO.getMessage(), false);
            }
        }
    }
}
