package co.lemnisk.controller;

import co.lemnisk.stream.DestTransformerStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/stream/start")
public class DestTransformerStreamController {

    @Autowired
    DestTransformerStream destTransformerStream;

    @GetMapping("dest_transformer")
    public void destinationTransformerStreamStarter() throws IOException {
        destTransformerStream.init();
    }

}
