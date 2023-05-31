package co.lemnisk.consumer.util;

import co.lemnisk.common.model.CDPDestinationInstance;
import co.lemnisk.common.service.DestinationInstanceMappingService;
import co.lemnisk.consumer.dto.MessageDTO;
import co.lemnisk.consumer.service.CDPDestinationApiDetailsService;
import co.lemnisk.consumer.service.RestClientService;
import co.lemnisk.consumer.service.s3.S3MessageLogService;
import co.lemnisk.consumer.utils.BeanUtil;
import co.lemnisk.consumer.utils.MessageQueue;
import co.lemnisk.consumer.utils.ParserUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * This class manages message queue to push records to the API endpoints checks every 5 milliseconds
 * for items in the queue and pushes them over to destination endpoints.
 *
 */

public class TopicQueueRunner implements Runnable {


	private static final Logger LOGGER = LoggerFactory.getLogger(TopicQueueRunner.class);
	private static ConcurrentLinkedQueue<MessageDTO> queue = new ConcurrentLinkedQueue<MessageDTO>();
	private static Thread topicQueueRunnerThread = null;
	private static TopicQueueRunner topicQueueRunner = null;
	private static final int BATCH_SIZE = 10;
	private static final int WAIT_TIME = 5;
	private static final int MAX_BYPASS = 5;

	public static final String TRANSFORMED_PATH = "$.transformedPayload";

	RestClientService restClientService;

	CDPDestinationApiDetailsService cdpDestinationApiDetailsService;

	DestinationInstanceMappingService destinationInstanceService;

	S3MessageLogService s3MessageLogService;

	/**
	 * Private constructor to prevent accidental creation of this object
	 */
	private TopicQueueRunner() {

	}

	public static TopicQueueRunner getInstance() {
		if (topicQueueRunner == null) {
			topicQueueRunner = new TopicQueueRunner();
			topicQueueRunnerThread = new Thread(topicQueueRunner);
			topicQueueRunnerThread.setDaemon(true);
			topicQueueRunnerThread.start();
		}
		return topicQueueRunner;
	}

	public void add(MessageDTO messageDTO) {
		queue.add(messageDTO);
	}

	public void run() {
		LOGGER.info("Destination Rest Client Thread Started");
		try {
			int bypassCounter = 0;

			restClientService = BeanUtil.getBean(RestClientService.class);
			cdpDestinationApiDetailsService = BeanUtil.getBean(CDPDestinationApiDetailsService.class);
			destinationInstanceService = BeanUtil.getBean(DestinationInstanceMappingService.class);
			s3MessageLogService = BeanUtil.getBean(S3MessageLogService.class);

			while (true) {

				MessageDTO messageDTO = MessageQueue.getInstance().getMessageQueue();
				if(messageDTO != null && messageDTO.getMessage() !=null && messageDTO.getMessage().length() > 0) {
					queue.add(messageDTO);
				}

				if (BATCH_SIZE <= queue.size() || bypassCounter > MAX_BYPASS) {
					try {
						if(queue.size() > 0){
							LOGGER.info("Total {} Messages Received. ", queue.size());
							for (MessageDTO messageDTOVar : queue) {

								Map<String, String> valuesMap = ParserUtils.getValues(messageDTOVar.getMessage());

								String transformedPayload = JsonUtils.getPayload(messageDTOVar.getMessage(), TRANSFORMED_PATH);
								CDPDestinationInstance cdpDestinationInstance = destinationInstanceService.getDestinationInstanceEntity(Integer.parseInt(valuesMap.get("destinationInstanceId")));

								if (cdpDestinationInstance != null && cdpDestinationInstance.getCdpDestinationId().equals(Constants.S3_DESTINATION_ID)){
									int sourceId = Integer.parseInt(valuesMap.get("srcId"));
									// Handle Write to S# Logs and Uploads
									String payload = StringEscapeUtils.unescapeJava(transformedPayload);

									try{
										s3MessageLogService.writeMessageToLogS3(messageDTOVar,payload,cdpDestinationInstance.getId(),sourceId);
									}
									catch (Exception e){
										LOGGER.error("Error while processing message for S3:  {} :Exception {}", messageDTOVar,e.getMessage());
									}

								}
								else{

									try {
										restClientService.restClientCall(messageDTOVar,transformedPayload,cdpDestinationInstance);
									} catch (IOException e) {
										e.printStackTrace();
										LOGGER.error("Error in processing the message {}", e);
									}
								}

								queue.remove();
							} // end of for
						}

					} catch (NoSuchElementException nse) {
						LOGGER.error("NoSuchElementException occurred");
						continue;
					}
					bypassCounter = 0;
				} else {
					bypassCounter++;
				}
				Thread.sleep(WAIT_TIME);
			}
		} catch (Exception e) {
			LOGGER.error("Exception while processing messages",e);
		}
	}
}

