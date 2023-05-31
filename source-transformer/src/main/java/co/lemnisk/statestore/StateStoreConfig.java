package co.lemnisk.statestore;

import co.lemnisk.common.constants.SourceTransformerConstant;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.apache.kafka.streams.state.WindowStore;
import org.springframework.stereotype.Component;

import java.time.Duration;

public class StateStoreConfig {

    public static StoreBuilder<WindowStore<String, Long>> config(String storeName) {
        Duration windowSize = Duration.ofMinutes(SourceTransformerConstant.DE_DUPE_WINDOW_SIZE_MIN);
        final Duration retentionPeriod = Duration.ofMinutes(SourceTransformerConstant.DE_DUPE_RETENTION_PERIOD_MIN);

        return Stores.windowStoreBuilder(
            Stores.persistentWindowStore(storeName,
                retentionPeriod,
                windowSize,
                false
            ),
            Serdes.String(),
            Serdes.Long());
    }
}
