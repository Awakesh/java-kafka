package co.lemnisk.controller;

import co.lemnisk.transform.analyzepost.AnalyzePostTransformerApp;
import co.lemnisk.transform.dmpnbaapi.DmpNbaApiTransformer;
import co.lemnisk.transform.dmpsstdata.DmpSstDataTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/stream/start")
public class StreamStarterController {

    @Autowired
    AnalyzePostTransformerApp analyzePostTransformerApp;

    @Autowired
    DmpSstDataTransformer dmpSstDataTransformer;

    @Autowired
    DmpNbaApiTransformer dmpNbaApiTransformer;

    @GetMapping("analyze_post")
    public void analyze_post() throws IOException {
        analyzePostTransformerApp.init();
    }

    @GetMapping("dmp_sst_data")
    public void dmp_sst_data() throws IOException {
        dmpSstDataTransformer.init();
    }

    @GetMapping("dmp_nba_di_api")
    public void dmp_nba_di_api() throws IOException {
        dmpNbaApiTransformer.init();
    }

}
