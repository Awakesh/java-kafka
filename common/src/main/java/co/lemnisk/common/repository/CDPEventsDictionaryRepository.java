package co.lemnisk.common.repository;

import co.lemnisk.common.model.CDPEventsDictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CDPEventsDictionaryRepository extends JpaRepository<CDPEventsDictionary, Integer> {

    CDPEventsDictionary getByCampaignIdAndEventNameAndEventType(Integer campaignId, String eventName, String eventType);

    CDPEventsDictionary getByCampaignIdAndEventName(Integer campaignId, String eventName);

}
