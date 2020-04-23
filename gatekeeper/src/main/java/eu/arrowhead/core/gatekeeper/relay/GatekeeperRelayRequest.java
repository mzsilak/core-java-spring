package eu.arrowhead.core.gatekeeper.relay;

import eu.arrowhead.api.common.exception.DataNotFoundException;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.dto.internal.GSDPollRequestDTO;
import eu.arrowhead.common.dto.internal.ICNProposalRequestDTO;
import org.springframework.util.Assert;

import javax.jms.MessageProducer;
import java.security.PublicKey;

public class GatekeeperRelayRequest {

	//=================================================================================================
	// members
	
	// data needed to answer 
	private final MessageProducer answerSender;
	private final PublicKey peerPublicKey;
	private final String sessionId;
	private final String messageType;
	
	private final Object payload;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public GatekeeperRelayRequest(final MessageProducer answerSender, final PublicKey peerPublicKey, final String sessionId, final String messageType, final Object payload) {
		Assert.notNull(answerSender, "Sender is null.");
		Assert.notNull(peerPublicKey, "Peer publc key is null.");
		Assert.isTrue(!Utilities.isEmpty(sessionId), "Session id is null or blank.");
		Assert.isTrue(!Utilities.isEmpty(messageType), "Message type is null or blank.");
		Assert.notNull(payload, "Payload is null.");
		
		this.answerSender = answerSender;
		this.peerPublicKey = peerPublicKey;
		this.sessionId = sessionId;
		this.messageType = messageType;
		this.payload = payload;
	}

	//-------------------------------------------------------------------------------------------------
	public MessageProducer getAnswerSender() { return answerSender; }
	public PublicKey getPeerPublicKey() { return peerPublicKey; }
	public String getSessionId() { return sessionId; }
	public String getMessageType() { return messageType; }

	//-------------------------------------------------------------------------------------------------
	public GSDPollRequestDTO getGSDPollRequest() {
		if (payload instanceof GSDPollRequestDTO) {
			return (GSDPollRequestDTO) payload;
		}
		
		throw new DataNotFoundException("The request is not a GSD poll.");
	}
	
	//-------------------------------------------------------------------------------------------------
	public ICNProposalRequestDTO getICNProposalRequest() {
		if (payload instanceof ICNProposalRequestDTO) {
			return (ICNProposalRequestDTO) payload;
		}
		
		throw new DataNotFoundException("The request is not an ICN proposal.");
	}
}