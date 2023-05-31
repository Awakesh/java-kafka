package co.lemnisk.data.migration.constants;

public class DataMigrationConstant {

    public static final String CONFLUENT_CONFIG_PATH="/disk1/config/kafka/confluent.properties";
    public static final String PROD_KAFKA_CONFIG_PATH="/disk1/config/data-migration/kafka-config.properties";

    public static class PayloadType {
        public static final String INCOMING = "INCOMING_PAYLOAD";
        public static final String OUTGOING = "OUTGOING_PAYLOAD";

    }
}
