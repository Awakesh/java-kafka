package co.lemnisk.common.tracing;

import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;

import io.opentelemetry.api.trace.Span;

public abstract class TransformTracingHelper<K, V, R> implements Transformer<K, V, R> {
    protected ProcessorContext context;
    String spanName;
    Tracing tracing;
    String topicName;

    public TransformTracingHelper(Tracing tracing, String spanName, String topicName) {
        this.spanName = spanName;
        this.tracing = tracing;
        this.topicName = topicName;
    }

    @Override
    public void close() {
    }

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public R transform(K key, V value) {
        Span span = TraceHelper.createSpan(tracing, context.headers(), spanName, topicName);
        try {
            return transformImpl(key, value, span);
        }
        finally {
            TraceHelper.closeSpan(context.headers(), span);
        }
    }
    
    public abstract R transformImpl(K key, V value, Span span);
}
