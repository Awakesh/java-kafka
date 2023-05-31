package co.lemnisk.common.tracing;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static co.lemnisk.common.tracing.TracingConstant.TRACER_NAME;

@Component
public class Tracing {
    @Getter
    private Tracer tracer;

    @Value("${jaeger.endpoint}")
    private String endpoint;

    @Value("${jaeger.service.name}")
    private String serviceName;

    @PostConstruct
    private void initTracer() {
        OpenTelemetry openTelemetry = JaegerConfiguration.initOpenTelemetry(endpoint, serviceName);
        this.tracer = openTelemetry.getTracer(TRACER_NAME);
    }

}
