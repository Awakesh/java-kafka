package co.lemnisk.common.service;

import co.lemnisk.common.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CampaignService {

    @Autowired
    CampaignRepository campaignRepository;

    public boolean isCampaignExist(String vizVrmId) {

        return campaignRepository.existsById(Integer.parseInt(vizVrmId));
    }
}