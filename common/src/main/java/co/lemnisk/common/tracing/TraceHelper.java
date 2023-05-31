package co.lemnisk.common.tracing;

import org.apache.kafka.common.header.Headers;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.TraceFlags;
import io.opentelemetry.api.trace.TraceState;
import io.opentelemetry.context.Context;

public class TraceHelper {
    public static final String TOPIC_TAG_STRING = "topic";

    public static Span createSpan(Tracing tracing, Headers headers, String spanName, String topicName) {
        SpanBuilder builder = tracing.getTracer().spanBuilder(spanName);
        if (headers != null &&
            headers.lastHeader(TracingConstant.TRACER_ID) != null &&
            headers.lastHeader(TracingConstant.SPAN_ID) != null) {
                SpanContext parent = SpanContext.create(new String(headers.lastHeader(TracingConstant.TRACER_ID).value()),
                new String(headers.lastHeader(TracingConstant.SPAN_ID).value()),
                TraceFlags.getSampled(),
                TraceState.getDefault());
                builder.setAttribute("opentracing.ref_type", "follows_from");
                builder.setParent(Context.current().with(Span.wrap(parent)));
        }
        Span span = builder.startSpan();
        span.setAttribute(TOPIC_TAG_STRING, topicName);
        return span;
    }

    public static void closeSpan(Headers headers, Span span) {
        if (headers != null) {
            headers.remove(TracingConstant.TRACER_ID).remove(TracingConstant.SPAN_ID);
            headers.add(TracingConstant.TRACER_ID, span.getSpanContext().getTraceId().getBytes());
            headers.add(TracingConstant.SPAN_ID, span.getSpanContext().getSpanId().getBytes());
        }
        span.end();
    }

    public static void handleException(Span span, Exception ex) {
        span.setStatus(StatusCode.ERROR, ex.getMessage());
        span.recordException(ex);
    }

    public static void addSpanAttributes(Span span, String eventType, String messageId, String campaignId) {
        if (span != null) {
            span.setAttribute(TracingConstant.Tags.CAMPAIGN_ID, campaignId);
            span.setAttribute(TracingConstant.Tags.MESSAGE_ID, messageId);
            span.setAttribute(TracingConstant.Tags.EVENT_TYPE, eventType);
        }
    }

    public static void addSpanAttributes(Span span, String eventType, String messageId, String campaignId, Integer destinationInstanceId) {
        if (span != null) {
            addSpanAttributes(span, eventType, messageId, campaignId);
            span.setAttribute(TracingConstant.Tags.DESTINATION_INSTANCE_ID, destinationInstanceId);
        }
    }
}
