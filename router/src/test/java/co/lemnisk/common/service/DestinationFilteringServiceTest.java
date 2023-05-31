package co.lemnisk.common.service;

import co.lemnisk.common.constants.Status;
import co.lemnisk.common.model.CDPEventsDictionary;
import co.lemnisk.common.repository.CDPEventsDictionaryRepository;
import co.lemnisk.common.repository.CDPSourceDestinationInstanceMappingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DestinationFilteringServiceTest {

    @Mock
    CDPEventsDictionaryRepository eventsDictionaryRepo;

    @Mock
    CDPSourceDestinationInstanceMappingRepository sourceDestInstanceMappingRepo;

    @InjectMocks
    DestinationFilteringService destinationFilteringService;

    @BeforeEach
    void setUp() {
        CDPEventsDictionary cdpEventsDictionary = new CDPEventsDictionary();
        cdpEventsDictionary.setId(1);
        cdpEventsDictionary.setCampaignId(6081);
        cdpEventsDictionary.setEventName("click");
        cdpEventsDictionary.setEventType("track");
        cdpEventsDictionary.setEventSchema("");
        cdpEventsDictionary.setAllowedDestList("[1,3,5]");

        List<Integer> destIdList = new LinkedList<>();
        for(int i = 1 ; i <= 10 ; i++) {
            destIdList.add(i);
        }

        when(eventsDictionaryRepo.getByCampaignIdAndEventNameAndEventType(anyInt(), anyString(), anyString()))
                .thenReturn(cdpEventsDictionary);

        when(sourceDestInstanceMappingRepo.getActiveMappedCDPDestinationInstanceIds(anyInt()))
                .thenReturn(destIdList);
    }

    @Test
    void getDestinationInstanceIdsTest() {
        Set<Integer> destinationInstanceIds = destinationFilteringService.getDestinationInstanceIds(6081, "click", "track", 100, true);

        assertEquals(3, destinationInstanceIds.size());

        assertThat(destinationInstanceIds, hasItems(1, 3, 5));

    }
}