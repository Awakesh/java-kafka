package co.lemnisk;

import co.lemnisk.common.Util;
import co.lemnisk.common.avro.model.event.track.web.TrackWeb;
import co.lemnisk.common.service.DestinationFilteringService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataMultiplierTest {

    private TrackWeb trackWebData;

    @Mock
    DestinationFilteringService destinationFilteringService;

    @InjectMocks
    DataMultiplier dataMultiplier;

    @BeforeEach
    void setUp() throws IOException {
        String rawData = getRawData();
        ObjectMapper objectMapper = new ObjectMapper();
        trackWebData = objectMapper.readValue(rawData, TrackWeb.class);

        Set<Integer> filterDestinationList = new LinkedHashSet<>();

        for(int i = 1; i <= 3 ; i ++) {
            filterDestinationList.add(i);
        }

        when(destinationFilteringService.getDestinationInstanceIds(6081, "accounts_otp:lead_verified", null, 100, true))
                .thenReturn(filterDestinationList);
    }

    private String getRawData() throws IOException {
        File file = Util.getFile("/fixtures/track-web.txt");
        return Util.readFileAsString(file);
    }

//    @Test
//    void createCopiesForEachDestInstanceTest() {
//        List<KeyValue<String, TrackWeb>> outputData = dataMultiplier.createCopiesForEachDestInstance("", trackWebData);
//
//        assertEquals(3, outputData.size());
//
//        List<Integer> destInstanceIds = new LinkedList<>();
//        for ( KeyValue<String, TrackWeb> i : outputData) {
//            destInstanceIds.add(Integer.valueOf((String) i.value.getLemEvent()));
//        }
//
//        assertThat(destInstanceIds, hasItems(1, 2, 3));
//    }
}