package co.lemnisk.transform.analyzepost.serde.l2;

import co.lemnisk.common.avro.model.event.identify.app.IdentifyApp;
import co.lemnisk.common.avro.model.event.identify.web.IdentifyWeb;
import co.lemnisk.common.avro.model.event.page.Page;
import co.lemnisk.common.avro.model.event.screen.Screen;
import co.lemnisk.common.avro.model.event.track.app.TrackApp;
import co.lemnisk.common.avro.model.event.track.web.TrackWeb;
import co.lemnisk.common.customavroserdes.CustomSpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;

import java.util.LinkedHashMap;
import java.util.Map;

public class EventSpecificAvroSerde {
    public static Map<String, Serde<?>> getSpecificAvroSerde(Map<String, String> conf, boolean isKey) {
        Map<String, Serde<?>> specificAvroSerde = new LinkedHashMap<>();

        // TODO: Need to change this serde to 'TrackWeb'
        final Serde<TrackWeb> trackWebSerde = new CustomSpecificAvroSerde<>();
        trackWebSerde.configure(conf, isKey);
        specificAvroSerde.put("trackWebSerde", trackWebSerde);

        final Serde<Page> pageSerde = new CustomSpecificAvroSerde<>();
        pageSerde.configure(conf, isKey);
        specificAvroSerde.put("pageSerde", pageSerde);

        final Serde<IdentifyWeb> identifyWebSerde = new CustomSpecificAvroSerde<>();
        identifyWebSerde.configure(conf, isKey);
        specificAvroSerde.put("identifyWebSerde", identifyWebSerde);

        final Serde<TrackApp> trackAppSerde = new CustomSpecificAvroSerde<>();
        trackAppSerde.configure(conf, isKey);
        specificAvroSerde.put("trackAppSerde", trackAppSerde);

        final Serde<Screen> screenSerde = new CustomSpecificAvroSerde<>();
        screenSerde.configure(conf, isKey);
        specificAvroSerde.put("screenSerde", screenSerde);

        final Serde<IdentifyApp> identifyAppSerde = new CustomSpecificAvroSerde<>();
        identifyAppSerde.configure(conf, isKey);
        specificAvroSerde.put("identifyAppSerde", identifyAppSerde);

        return specificAvroSerde;
    }
}
