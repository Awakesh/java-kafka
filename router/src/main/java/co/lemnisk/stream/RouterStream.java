package co.lemnisk.stream;

import co.lemnisk.common.kafka.configuration.KafkaConfluentConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

@Component
public class RouterStream {

    private static final Logger uncaughtExceptionLogger = LoggerFactory.getLogger("CustomUncaughtExceptionLogger");

    @Value("${kafka.stream.router.id}")
    private String streamAppId;

    @Value("${kafka.stream.router.thread}")
    private String threadCount;

    @Autowired
    RouterStreamTopology routerStreamTopology;

    public void init() throws IOException {

        Properties config = KafkaConfluentConfig.streamConfig();

        config.put(StreamsConfig.APPLICATION_ID_CONFIG, streamAppId);
        config.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, threadCount);

        Map<String, String> avroConfig = (Map)config;

        Topology topology = routerStreamTopology.builder(avroConfig, false);

        KafkaStreams streams = new KafkaStreams(topology, config);

        streams.setUncaughtExceptionHandler(ex -> {
            uncaughtExceptionLogger.error("Kafka-Streams uncaught exception occurred:" + ex);
            return StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse.REPLACE_THREAD;
        });

        final CountDownLatch latch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread("stop-router-stream-application") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }

        System.exit(0);

    }
}
