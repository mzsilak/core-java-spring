package eu.arrowhead.core.gatekeeper.service;

import eu.arrowhead.api.common.exception.BadPayloadException;
import eu.arrowhead.api.common.exception.InvalidParameterException;
import eu.arrowhead.api.common.exception.TimeoutException;
import eu.arrowhead.api.common.model.ErrorMessageDTO;
import eu.arrowhead.api.common.model.ErrorWrapperDTO;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.dto.internal.GSDPollRequestDTO;
import eu.arrowhead.common.dto.internal.GSDPollResponseDTO;
import eu.arrowhead.core.gatekeeper.relay.GatekeeperRelayClient;
import eu.arrowhead.core.gatekeeper.relay.GatekeeperRelayResponse;
import eu.arrowhead.core.gatekeeper.relay.GeneralAdvertisementResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import javax.jms.Session;
import java.util.concurrent.BlockingQueue;

public class GSDPollTask implements Runnable {
	
	//=================================================================================================
	// members
	
	private final Logger logger = LogManager.getLogger(GSDPollTask.class);
	
	private final GatekeeperRelayClient relayClient;
	private final Session session;
	private final String recipientCloudCN;
	private final String recipientCloudPublicKey;
	private final GSDPollRequestDTO gsdPollRequestDTO;
	private final BlockingQueue<ErrorWrapperDTO> queue;

	//=================================================================================================
	// methods
		
	//-------------------------------------------------------------------------------------------------
	public GSDPollTask(final GatekeeperRelayClient relayClient, final Session session, final String recipientCloudCN, final String recipientCloudPublicKey, final GSDPollRequestDTO gsdPollRequestDTO,
					   final BlockingQueue<ErrorWrapperDTO> queue) {
		Assert.notNull(relayClient, "relayClient is null");
		Assert.notNull(session, "session is null");
		Assert.isTrue(!Utilities.isEmpty(recipientCloudCN), "recipientCloudCN is empty");
		Assert.isTrue(!Utilities.isEmpty(recipientCloudPublicKey), "recipientCloudCN is empty");
		Assert.notNull(gsdPollRequestDTO, "gsdPollRequestDTO is null");
		Assert.notNull(queue, "queue is null");
		
		this.relayClient = relayClient;
		this.session = session;
		this.recipientCloudCN = recipientCloudCN;
		this.recipientCloudPublicKey = recipientCloudPublicKey;
		this.gsdPollRequestDTO = gsdPollRequestDTO;
		this.queue = queue;		
	}

	//-------------------------------------------------------------------------------------------------	
	@Override
	public void run() {
		try {
			logger.debug("GDSPollTask.run started...");
			
			if (Thread.currentThread().isInterrupted()) {
				logger.trace("Thread {} is interrupted...", Thread.currentThread().getName());
				queue.add(new GSDPollResponseDTO());
				return;
			}
										
			final GeneralAdvertisementResult result = relayClient.publishGeneralAdvertisement(session, recipientCloudCN, recipientCloudPublicKey);
			if (result == null) {
				throw new TimeoutException(recipientCloudCN + " cloud: GeneralAdvertisementResult timeout");
			}
			
			final GatekeeperRelayResponse response = relayClient.sendRequestAndReturnResponse(session, result , gsdPollRequestDTO);
			if (response == null) {
				throw new TimeoutException(recipientCloudCN + " cloud: GatekeeperRelayResponse timeout");
			}
			
			queue.add(response.getGSDPollResponse());
		} catch (final InvalidParameterException | BadPayloadException ex) {	
			// We forward this two type of arrowhead exception to the caller
			logger.debug("Exception: {}", ex.getMessage());
			queue.add(new ErrorMessageDTO(ex));
		} catch (final Throwable ex) {			
			// Must catch all throwable, otherwise the blocking queue would block the whole process
			logger.debug("Exception: {}", ex.getMessage());
			
			// adding empty responseDTO into the blocking queue in order to having exactly as many response as request was sent
			queue.add(new GSDPollResponseDTO()); 			
		}
	}
}