package co.lemnisk;

import co.lemnisk.common.service.DestinationInstanceMappingService;
import co.lemnisk.common.service.TransformerCacheableData;
import co.lemnisk.common.service.TransformerFunctionService;
import co.lemnisk.stream.DestTransformerStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@EnableCaching
@SpringBootApplication
@EnableScheduling
public class DestinationTransformerApplication {
    @Autowired
    TransformerCacheableData transformerCacheableData;

    @Autowired
    DestinationInstanceMappingService destinationInstanceService;

    private static ConfigurableApplicationContext appContext;

    public static void main(String[] args) {
        appContext = SpringApplication.run(DestinationTransformerApplication.class, args);

        DestinationTransformerApplication app = appContext.getBean(DestinationTransformerApplication.class);
        app.run();
    }

    public void run() {
        queryToCache();

        DestTransformerStream stream = appContext.getBean(DestTransformerStream.class);
        Runnable r1 = () -> { try { stream.init(); } catch (IOException ignore) {} };

        ExecutorService service = Executors.newFixedThreadPool(1);
        service.submit(r1);

        service.shutdown();

        try { service.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS); }
        catch (InterruptedException e) { e.printStackTrace(); }
    }

    private void queryToCache() {
        transformerCacheableData.getAllStandardEventsTransformers();
        transformerCacheableData.getAllActiveCustomEventsTransformers();
        transformerCacheableData.getAllPassThroughEventsTransformers();
        destinationInstanceService.getAllActiveDestinationInstances();
    }
}
