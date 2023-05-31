package co.lemnisk.common.tracing;

import org.apache.kafka.streams.kstream.ValueTransformer;
import org.apache.kafka.streams.processor.ProcessorContext;

import io.opentelemetry.api.trace.Span;

public abstract class ValueTransformTraceHelper<V, VR> implements ValueTransformer<V, VR> {
    ProcessorContext context;
    String spanName;
    Tracing tracing;
    String topicName;

    public ValueTransformTraceHelper(Tracing tracing, String spanName, String topicName) {
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
    public VR transform(V rawData) {
        Span span = TraceHelper.createSpan(tracing, context.headers(), spanName, topicName);
        try {
            return transformImpl(rawData, span);
        }
        finally {
            TraceHelper.closeSpan(context.headers(), span);
        }
    }

    public abstract VR transformImpl(V data, Span span);
}
