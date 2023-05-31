package co.lemnisk;

import co.lemnisk.common.service.CommonCacheableData;
import co.lemnisk.common.service.RouterCacheableData;
import co.lemnisk.stream.RouterStream;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@SpringBootApplication
@EnableScheduling
public class RouterApplication {

    @Autowired
    CommonCacheableData commonCacheableData;

    @Autowired
    RouterCacheableData routerCacheableData;

    private static ConfigurableApplicationContext appContext;

    public static void main(String[] args) {
        appContext = SpringApplication.run(RouterApplication.class, args);

        RouterApplication app = appContext.getBean(RouterApplication.class);
        app.run();
    }

    private void run() {
        queryToCache();

        RouterStream routerStream = appContext.getBean(RouterStream.class);
        Runnable r1 = () -> { try { routerStream.init(); } catch (IOException ignore) {} };

        ExecutorService service = Executors.newFixedThreadPool(1);
        service.submit(r1);

        service.shutdown();

        try { service.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS); }
        catch (InterruptedException e) { e.printStackTrace(); }
    }

    private void queryToCache() {
        routerCacheableData.getCloudModeDestinationInstanceIds();
        commonCacheableData.getAllActiveCustomEventsData();
        commonCacheableData.getAllStandardEventsData();
    }
}
