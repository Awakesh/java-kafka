**Notes for how to run this**

_Pre-requisites_

Create confluent.properties (from confluent.properties.template)

Fill the appropriate values.

`mvn clean package

mvn exec:java -Dexec.mainClass="co.lemnisk.events.backend.TransformerApp"`

We are using confluent cloud for event dev environment for now

Please read this

https://docs.confluent.io/platform/current/tutorials/examples/ccloud/docs/beginner-cloud.html

Once you are done with setting up ccloud environment/login etc

```shell
praveends@Praveens-MacBook-Air events.processor % ccloud api-key store --resource lkc-xd5pz

praveends@Praveens-MacBook-Air events.processor % ccloud api-key use EKIPNLIRGT2LSFAJ --resource lkc-xd5pz

praveends@Praveens-MacBook-Air events.processor % ccloud kafka topic produce pds-dataflow < data/test.json

praveends@Praveens-MacBook-Air events.processor % ccloud kafka topic consume pds-translated -b
```


**Few items to read**

https://www.confluent.io/confluent-cloud-vs-amazon-msk/

https://www.confluent.io/blog/avro-kafka-data/


## Steps to start the Transformer application:

- Start the `src/main/java/co/lemnisk/EventProcessorApplication.java` (Spring Boot Application)
- Currently, there are two transformers present in our application:
  1. AnalyzePostTransformerApp (for transforming the messages present in `analyze_post` topic)
  2. DmpSstDataTransformerApp (for transforming the messages present in `dmp_sst_data` topic)
- Temporarily, we've disabled the auto-start functionality of both the transformers. 
- So, you need to manually start each of the transformer using the get requests (StreamStarterController is added for this)
  1. For starting AnalyzePostTransformerApp: `localhost:8080/stream/start/analyze_post`
  2. For starting DmpSstDataTransformerApp: `localhost:8080/stream/start/dmp_sst_data`
- Insert records in **source** topics: 
  1. `dmp_sst_data`, 
  2. `analyze_post`
- Check transformed records present in **sink** topics:
  1. `page_event_web`, 
  2. `track_event_mobile`, 
  3. `track_event_web`,
  4. `screen_event_mobile`, 
  5. `identify_event_mobile`,
  6. `identify_event_web`.


## Few commands to produce and consume messages from the Kafka Topics:

**Produce messages in kafka source topic using:**
```shell
confluent kafka topic produce ${topic_name} < ${file_path}

# Examples:
confluent kafka topic produce analyze_post-local < data/analyze_post-topic/v1/raw/page-web.txt
confluent kafka topic produce dmp_sst_nba_di_api-local < source-transformer/data/dmp_nba_di_api/raw/encrypted-data.txt
```
**Consume transformed AVRO messages from kafka sink topic using:**
```shell
confluent kafka topic consume ${topic_name} -b --value-format avro
eg. confluent kafka topic consume page_event_web -b --value-format avro
```

### Sample data for DmpNbaDiApi transformer

```json
{"bouncerId":"BO6374","srcid":"2","hashed_email":"72841709b250cc55a661f7b73d6a6671b09b4957f0a9d0205a8e2f0d0ef12896","external_id":"a690a74b0113df362fd41f168446201b0c424d75a58bdc3679937d76a48b5e5e","eventtime":1663657466,"id":"8d2efd84-d49d-4811-99cf-167cf6008282","type":"track","userid":"CX1436907","eventname":"launchpad-backend_project:status_changed","properties":{"designerId":"0","newStatus":"inactive","oldStatus":"INACTIVE","statusUpdateReason1":"-","sourceSystem":"EAGLE","statusUpdateReason2":"-","customerId":"1436907","projectId":"1388374","updatedAt":"2022-09-20T07:04:25+00:00"}}
```