package co.lemnisk.data.migration;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CampaignFilter {

    private final Logger logger = LoggerFactory.getLogger(CampaignFilter.class.getName());

    @Value("${list.campaigns.ids.allowed}")
    private List<String> allowedCampaignIds;

    public Boolean allowedCampaign(String payload, String topic) {

        try {
            return allowedCampaignIds.contains(getCampaignId(payload, topic));
        }
        catch (Exception ex) {
            logger.warn("Got Exception while filtering data by Campaign. Exception Details: {}", ex.getMessage());
            ex.printStackTrace();

            // Allow if there's any exception as it'll get filtered out in the next phase/services
            // Later we have also implemented DLT for this
            return true;
        }
    }

    /* Topics:
        - analyze_post (v1)
        - analyze_post (v2)
        - analyze_post (v3)
        - dmp_sst_data
        - dmp_sst_nba_di_api
    * */
    private String getCampaignId(String payload, String topic) {

        LinkedTreeMap<String, String> parsedData = (LinkedTreeMap<String, String>) new Gson().fromJson(payload, Object.class);

        String campaignId = parsedData.get("campaignId");

        if (campaignId == null) {
            throw new RuntimeException("Campaign Id not found");
        }

        return campaignId.replace("VIZVRM", "");
    }
}
