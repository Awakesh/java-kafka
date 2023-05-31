package co.lemnisk;

import co.lemnisk.common.service.CommonCacheableData;
import co.lemnisk.transform.analyzepost.AnalyzePostTransformerApp;
import co.lemnisk.transform.dmpnbaapi.DmpNbaApiTransformer;
import co.lemnisk.transform.dmpsstdata.DmpSstDataTransformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class SourceTransformerApplication {

    @Autowired
    CommonCacheableData commonCacheableData;

    Logger logger = LoggerFactory.getLogger(SourceTransformerApplication.class.getName());

    private static ConfigurableApplicationContext appContext;

    public static void main(String[] args) {
        appContext = SpringApplication.run(SourceTransformerApplication.class, args);

        SourceTransformerApplication app = appContext.getBean(SourceTransformerApplication.class);
        app.run();
    }

    private void run() {
        queryToCache();

        AnalyzePostTransformerApp analyzePostTransformerApp = appContext.getBean(AnalyzePostTransformerApp.class);
        Runnable r1 = () -> {
            try { analyzePostTransformerApp.init(); } catch (IOException ex) {
            logger.warn("Exception while starting AnalyzePost Transformer");
            logger.warn("Error Message: {}\nStacktrace: {}", ex.getMessage(), ex.getStackTrace());
        } };

        DmpNbaApiTransformer dmpNbaApiTransformer = appContext.getBean(DmpNbaApiTransformer.class);
        Runnable r2 = () -> { try { dmpNbaApiTransformer.init(); } catch (IOException ex) {
            logger.warn("Exception while starting DmpNbaApi Transformer");
            logger.warn("Exception Message: {}\nStacktrace: {}", ex.getMessage(), ex.getStackTrace());
        } };

        DmpSstDataTransformer dmpSstDataTransformer = appContext.getBean(DmpSstDataTransformer.class);
        Runnable r3 = () -> { try { dmpSstDataTransformer.init(); } catch (IOException ex) {
            logger.warn("Exception while starting DmpSstData Transformer");
            logger.warn("Message: {}\nStacktrace: {}", ex.getMessage(), ex.getStackTrace());
        } };

        ExecutorService service = Executors.newFixedThreadPool(9);
        service.submit(r1);
        service.submit(r2);
        service.submit(r3);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {

                logger.info("Performing some shutdown cleanup...");
                service.shutdown();
                while (true) {
                    try {
                        logger.info("Waiting for the service to terminate...");
                        if (service.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS)) {
                            break;
                        }
                    } catch (InterruptedException e) {
                    }
                }
                logger.info("Done cleaning");
            }
        }));
    }

    private void queryToCache() {
        commonCacheableData.getAllActiveCustomEventsData();
        commonCacheableData.getAllStandardEventsData();
    }
}
