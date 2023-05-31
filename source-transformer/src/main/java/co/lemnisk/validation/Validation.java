package co.lemnisk.validation;

import co.lemnisk.common.constants.Constants;
import co.lemnisk.common.constants.ExceptionMessage;
import co.lemnisk.common.constants.SourceTransformerConstant;
import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.common.metrics.MonitoringConstant;
import co.lemnisk.common.metrics.EventMonitoring;

import java.time.Instant;

import static co.lemnisk.common.constants.SourceTransformerConstant.ALLOWED_INCOMING_DURATION_MIN;

public class Validation {

    private final EventMonitoring eventMonitoring = EventMonitoring.getInstance();

    public void validate(String messageId, String ipAddress, String accountId, String serverTs, String srcId) {

        if (!isValidAccountId(accountId)){
            throw new TransformerException(ExceptionMessage.INVALID_CAMPAIGN_ID + ": " + accountId);
        }

        if (srcId == null || srcId.isBlank()) {
            eventMonitoring.addProcessingErrorMetrics(accountId, MonitoringConstant.SPAN_ID_ADDRESS_ERROR, Constants.ModuleNames.SOURCE_TRANSFORMER);
            throw new TransformerException(ExceptionMessage.INVALID_SRC_ID + ": " + srcId);
        }

        if (ipAddress == null || ipAddress.isBlank()) {
            eventMonitoring.addProcessingErrorMetrics(accountId, MonitoringConstant.IP_ADDRESS_ERROR, Constants.ModuleNames.SOURCE_TRANSFORMER);
            throw new TransformerException(ExceptionMessage.INVALID_IP + ": " + ipAddress);
        }

        validateServerTs(accountId, serverTs);
    }

    public void validateDmpNba(String srcId, String serverTs, String accountId){

        if (!isValidAccountId(accountId)) {
            throw new TransformerException(ExceptionMessage.INVALID_CAMPAIGN_ID + ": " + accountId);
        }

        if (srcId == null || srcId.isBlank()) {
            throw new TransformerException(ExceptionMessage.INVALID_SRC_ID + ": " + srcId);
        }

        validateServerTs(accountId, serverTs);
    }

    private boolean isValidAccountId(String accountId) {
        return accountId != null
            && SourceTransformerConstant.ALLOWED_ACCOUNT_IDS.contains(accountId);
    }

    private void validateServerTs(String accountId, String serverTs) {

        if (serverTs == null || serverTs.isBlank()) {
            eventMonitoring.addProcessingErrorMetrics(accountId, MonitoringConstant.SERVER_TS, Constants.ModuleNames.SOURCE_TRANSFORMER);
            throw new TransformerException(ExceptionMessage.BLANK_SERVER_TIMESTAMP + ": " + serverTs);
        }
        else if(Instant.ofEpochMilli(Long.parseLong(serverTs)).isAfter(Instant.now().plus(ALLOWED_INCOMING_DURATION_MIN)))
        {
            eventMonitoring.addProcessingErrorMetrics(accountId, MonitoringConstant.SERVER_TS, Constants.ModuleNames.SOURCE_TRANSFORMER);
            throw new TransformerException(ExceptionMessage.INVALID_SERVER_TIMESTAMP + ": " + serverTs);
        }
    }
}
