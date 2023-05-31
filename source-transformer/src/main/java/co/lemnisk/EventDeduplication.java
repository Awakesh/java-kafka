package co.lemnisk;

import co.lemnisk.common.tracing.TracingConstant;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.TraceFlags;
import io.opentelemetry.api.trace.TraceState;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;

import org.apache.kafka.streams.state.WindowStore;
import org.apache.kafka.streams.state.WindowStoreIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static co.lemnisk.common.constants.SourceTransformerConstant.PAYLOAD_DE_DUPE_INTERVAL;

public class EventDeduplication<K, V, R> implements Transformer<K, V, KeyValue<K, V>> {

    private WindowStore<R, Long> eventIdStore;
    private final KeyValueMapper<K, V, R> param;
    private final String storeName;
    private ProcessorContext context;

    private final Logger logger = LoggerFactory.getLogger(EventDeduplication.class);
    private final Tracer tracer = GlobalOpenTelemetry.getTracer(TracingConstant.TRACER_NAME);

    public EventDeduplication(String storeName, final KeyValueMapper<K, V, R> param) {
        this.param = param;
        this.storeName = storeName;
    }

    @Override
    public void init(final ProcessorContext context) {
        this.context = context;
        eventIdStore = context.getStateStore(storeName);
    }

    public KeyValue<K, V> transform(final K key, final V value) {
        SpanContext parent = SpanContext.create(new String(context.headers().lastHeader(TracingConstant.TRACER_ID).value()),
            new String(context.headers().lastHeader(TracingConstant.SPAN_ID).value()),
            TraceFlags.getSampled(),
            TraceState.getDefault());
        SpanBuilder builder = tracer.spanBuilder(TracingConstant.SpanNames.EVENT_DEDUPLICATION);

        builder.setAttribute("opentracing.ref_type", "follows_from");
        builder.setParent(Context.current().with(Span.wrap(parent)));

        Span span = builder.startSpan();
        try {
            if (value == null) {
                span.addEvent("Dropping null value");
                return KeyValue.pair(key, null);
            }

            R eventId = param.apply(key, value);

            if (eventId == null) {
                return KeyValue.pair(key, value);
            } else {
                final KeyValue<K, V> output;

                if (isDuplicate(eventId)) {
                    logger.info("Duplicate Event for eventID: {}", eventId);
                    output = null;
                    span.addEvent("Duplicate record found for messageId: " + eventId);
                    updateTimestampOfExistingEventToPreventExpiry(eventId, context.timestamp());
                } else {
                    output = KeyValue.pair(key, value);
                    span.addEvent("No duplicate record found for messageId: " + eventId);
                    rememberNewEvent(eventId, context.timestamp());
                }
                return output;
            }
        }
        finally {
            span.end();
        }
    }

    private boolean isDuplicate(R eventId) {
        final long eventTime = context.timestamp();
        long timeFrom = eventTime - PAYLOAD_DE_DUPE_INTERVAL;

        final WindowStoreIterator<Long> timeIterator = eventIdStore.fetch( eventId, timeFrom, eventTime);
        final boolean isDuplicate = timeIterator.hasNext();
        timeIterator.close();

        return isDuplicate;
    }

    private void updateTimestampOfExistingEventToPreventExpiry(R eventId, final long newTimestamp) {
        eventIdStore.put(eventId, newTimestamp, newTimestamp);
    }

    private void rememberNewEvent(R eventId, final long timestamp) {
        eventIdStore.put(eventId, timestamp, timestamp);
    }

    @Override
    public void close() {

    }
}
