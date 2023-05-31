package co.lemnisk.consumer.service;

import java.io.IOException;

import co.lemnisk.common.model.CDPDestinationInstance;
import co.lemnisk.consumer.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import co.lemnisk.consumer.util.RestClientUtil;

@Service
public class RestClientService {
	@Autowired
	RestClientUtil restClientUtil;
	

	@Async("restClientExecutor")
	public void restClientCall(MessageDTO data, String transformedPayload, CDPDestinationInstance cdpDestinationInstance ) throws IOException {
		restClientUtil.process(data,transformedPayload,cdpDestinationInstance);
	}

}
