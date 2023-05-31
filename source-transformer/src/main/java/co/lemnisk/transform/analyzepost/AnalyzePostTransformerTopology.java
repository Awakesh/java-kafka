package co.lemnisk.transform.analyzepost;

import co.lemnisk.statestore.StateStoreConfig;
import co.lemnisk.transform.analyzepost.serde.l2.EventSpecificAvroSerde;
import co.lemnisk.transform.analyzepost.topology.l2.L2V3TransformerTopology;
import co.lemnisk.transform.analyzepost.topology.l2.L2V1TransformerTopology;
import co.lemnisk.transform.analyzepost.topology.l2.L2V2TransformerTopology;
import co.lemnisk.transform.analyzepost.topology.l1.L1TransformerTopology;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@NoArgsConstructor
public class AnalyzePostTransformerTopology {

    @Autowired
    L1TransformerTopology l1TransformerTopology;

    @Autowired
    L2V3TransformerTopology l2V3TransformerTopology;

    @Autowired
    L2V2TransformerTopology l2V2TransformerTopology;

    @Autowired
    L2V1TransformerTopology l2V1TransformerTopology;

    @Value("${analyze_post.state_store.name}")
    private String stateStoreName;

    public Topology builder(Map<String, String> conf, boolean isKey) {

        StreamsBuilder builder = new StreamsBuilder();

        Map<String, Serde<?>> specificAvroSerde = EventSpecificAvroSerde.getSpecificAvroSerde(conf, isKey);

        /////////////// L1  Classifier ///////////////
        l1TransformerTopology.setBuilder(builder);
        l1TransformerTopology.transform();

        builder.addStateStore(StateStoreConfig.config(stateStoreName));

        ///////// L2 V1 streams ///////////////
        l2V1TransformerTopology.setBuilder(builder);
        l2V1TransformerTopology.transform(specificAvroSerde);
//
        /////////////// L2 V2 streams ///////////////
        l2V2TransformerTopology.setBuilder(builder);
        l2V2TransformerTopology.transform(conf, isKey);

        /////////////// L2 V3 streams ///////////////
        l2V3TransformerTopology.setBuilder(builder);
        l2V3TransformerTopology.transform(conf, isKey);

        return builder.build();
    }

}
