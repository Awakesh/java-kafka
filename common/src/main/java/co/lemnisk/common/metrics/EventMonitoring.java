package co.lemnisk.common.metrics;

import com.vizury.metrics.MonitoringStatistics;
import com.vizury.metrics.config.SectionConfigIni;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class EventMonitoring {

    private static final String DEFAULT_METRICS_PATH = "/disk1/config/metrics/properties/metric.ini";
    private static final String DEFAULT_METRICS_DUMPER_PATH = "/disk1/config/metrics/properties/metricDumper.ini";

    private static final String METRICS_PATH = "/disk1/config/metrics/properties/metric.ini";
    private static final String METRICS_DUMPER_PATH = "/disk1/config/metrics/properties/metricDumper.ini";

    private static final String CDP_EVENTS_METRICS = "cdp_events";
    private static final String CDP_EVENT_PROPERTIES_METRIC = "cdp_event_properties";
    private static final String CDP_PROCESSING_ERROR_METRICS = "cdp_processing_error_metrics";
    private static final String CDP_DEST_RESPONSE_METRICS = "cdp_dest_response";
    private static final String CDP_DEST_UPLOADS = "cdp_destination_upload";
    private static final String CDP_DEST_EVENTS = "cdp_dest_events";
    private static final boolean DISABLE_MONITORING = false;

    private MonitoringStatistics monitoringStatistics;
    private static EventMonitoring eventMonitoring;

    // TODO: Use this only where instance is created using `new` keyword
    //       Later remove this and use this class as Autowired everywhere
    public static EventMonitoring getInstance() {

        if (eventMonitoring == null) {
            eventMonitoring = new EventMonitoring();
            eventMonitoring.setup();
        }

        return eventMonitoring;
    }

    @PostConstruct
    private void setup() {
        if (DISABLE_MONITORING || monitoringStatistics != null) {
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

    public void addCdpMetric(String campaignId, String cdpSourceInstanceId, String eventName, String type, String componentName) {

        if (monitoringStatistics != null) {
            monitoringStatistics.addMetric(CDP_EVENTS_METRICS, 1, campaignId, cdpSourceInstanceId, eventName, type, componentName);
        }
    }

    public void addCdpEventPropertiesMetric(String campaignId,
                                            String srcId,
                                            String eventName,
                                            Map<CharSequence, Object> properties,
                                            String type) {

        if (monitoringStatistics != null) {
            monitoringStatistics.addMetric(CDP_EVENT_PROPERTIES_METRIC, 1, campaignId, srcId, eventName, properties.toString(), type);
        }
    }

    public void addProcessingErrorMetrics(String campaignId, String errorType, String componentName){

        if (monitoringStatistics != null){
            monitoringStatistics.addMetric(CDP_PROCESSING_ERROR_METRICS, 1, campaignId, componentName, errorType);
        }
    }

    public void addDestResponseMetrics(String sourceId, String campaignId, String destinationId, String responseCode){

        if (monitoringStatistics != null){
            monitoringStatistics.addMetric(CDP_DEST_RESPONSE_METRICS, 1, sourceId, campaignId, destinationId, responseCode);
        }
    }

    public void addDestUploadResponseMetrics(String sourceId, String destinationId, String uploadType , String fileName){
        if (monitoringStatistics != null){
            monitoringStatistics.addMetric(CDP_DEST_UPLOADS, 1, sourceId,  destinationId, uploadType , fileName);
        }
    }

    public void addCdpDestEventMetrics(String sourceId, String destinationId, String campaignId){
        if (monitoringStatistics != null) {
            monitoringStatistics.addMetric(CDP_DEST_EVENTS, 1, sourceId, destinationId, campaignId);
        }
    }

}
