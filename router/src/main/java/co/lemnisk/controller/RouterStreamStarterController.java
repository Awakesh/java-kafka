package co.lemnisk.controller;

import co.lemnisk.stream.RouterStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/stream/start")
public class RouterStreamStarterController {

    @Autowired
    RouterStream routerStream;

    @GetMapping("router")
    public void routerStreamStarter() throws IOException {
        routerStream.init();
    }

}
