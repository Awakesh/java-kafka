package co.lemnisk.transform.dmpnbaapi.model;

import co.lemnisk.common.constants.Constants;
import co.lemnisk.common.constants.ExceptionMessage;
import co.lemnisk.common.constants.SourceTransformerConstant;
import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.transform.CampaignIdParser;
import co.lemnisk.utils.encryption.KmsEncryption;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class DmpNbaApiInputPayload {

    private String data;
    private String serverTs;
    private String campaignId;

    @Getter
    private String rawData;

    static final Logger logger = LoggerFactory.getLogger(DmpNbaApiInputPayload.class);

    public void process(String rawData) {
        this.rawData = rawData;
        String[] splitData = rawData.split(SourceTransformerConstant.PAYLOAD_SEPARATOR);
        this.serverTs = splitData[1];

        Gson gson = new Gson();
        LinkedTreeMap<String, String> parsedData = (LinkedTreeMap<String, String>) gson.fromJson(splitData[3], Object.class);
        this.data = decryptData(parsedData);
    }

    private String decryptData(LinkedTreeMap<String, String> parsedData) {

        String decryptedData;

        this.campaignId = parsedData.get("campaignId");
        String encryptedKey = parsedData.get("encryptedKey");
        String encryptedData = parsedData.get("data");

        this.campaignId = String.valueOf(CampaignIdParser.parse(campaignId));

        if (isValidAccountId(campaignId)) {
            logger.info("Decrypting data with Campaign ID: {}", campaignId);

            KmsEncryption kmsEncryption = new KmsEncryption();
            decryptedData = kmsEncryption.decrypt(encryptedData, encryptedKey, campaignId, Constants.ModuleNames.SOURCE_TRANSFORMER);
        }
        else {
            throw new TransformerException(ExceptionMessage.INVALID_CAMPAIGN_ID + ": " + campaignId);
        }

        logger.info("ServerTs: {} \t decryptedData: {}", serverTs, decryptedData);

        return decryptedData;
    }

    private boolean isValidAccountId(String accountId) {
        return accountId != null
                && SourceTransformerConstant.ALLOWED_ACCOUNT_IDS.contains(accountId);
    }

}
