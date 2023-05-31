package co.lemnisk.data.migration.metrics;

import com.vizury.metrics.MonitoringStatistics;
import com.vizury.metrics.config.SectionConfigIni;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class EventMonitoring {

    private static final String DEFAULT_METRICS_PATH = "/disk1/config/metrics/properties/metric.ini";
    private static final String DEFAULT_METRICS_DUMPER_PATH = "/disk1/config/metrics/properties/metricDumper.ini";

    private static final String METRICS_PATH = "/disk1/config/metrics/properties/metric.ini";
    private static final String METRICS_DUMPER_PATH = "/disk1/config/metrics/properties/metricDumper.ini";

    @Value("${kdm.disable.monitoring}")
    private boolean disableMonitoring;

    private final String dataMigrationMetrics = "cdp_kafka_data_migration";

    private MonitoringStatistics monitoringStatistics;

    @PostConstruct
    private void setup() {
        if (disableMonitoring || monitoringStatistics != null) {
            return;
        }

        SectionConfigIni metrics = new SectionConfigIni(METRICS_PATH, DEFAULT_METRICS_PATH);
        SectionConfigIni metricsCollector = new SectionConfigIni(METRICS_DUMPER_PATH, DEFAULT_METRICS_DUMPER_PATH);

        monitoringStatistics = new MonitoringStatistics(metrics, metricsCollector, 1000, 1000);
        Thread thread = new Thread(monitoringStatistics);
        thread.start();

        setupShutdownHook();
    }

    private void setupShutdownHook() {
        Thread thread = new Thread(() -> monitoringStatistics.shutdown());

        Runtime.getRuntime().addShutdownHook(thread);
    }

    public void addPayloadMetric(String sourceTopic, String destinationTopic, String payloadType) {

        if (monitoringStatistics != null) {
            monitoringStatistics.addMetric(dataMigrationMetrics, 1, sourceTopic, destinationTopic, payloadType);
        }
    }

}
