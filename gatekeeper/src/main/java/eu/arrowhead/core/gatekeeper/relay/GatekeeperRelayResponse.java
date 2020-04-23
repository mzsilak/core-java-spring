package eu.arrowhead.core.gatekeeper.relay;

import eu.arrowhead.api.common.exception.DataNotFoundException;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.dto.internal.GSDPollResponseDTO;
import eu.arrowhead.common.dto.internal.ICNProposalResponseDTO;
import org.springframework.util.Assert;

public class GatekeeperRelayResponse {
	
	//=================================================================================================
	// members
	
	private final String sessionId;
	private final String messageType;
	
	private final Object payload;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public GatekeeperRelayResponse(final String sessionId, final String messageType, final Object payload) {
		Assert.isTrue(!Utilities.isEmpty(sessionId), "Session id is null or blank.");
		Assert.isTrue(!Utilities.isEmpty(messageType), "Message type is null or blank.");
		Assert.notNull(payload, "Payload is null.");
		
		this.sessionId = sessionId;
		this.messageType = messageType;
		this.payload = payload;
	}

	//-------------------------------------------------------------------------------------------------
	public String getSessionId() { return sessionId; }
	public String getMessageType() { return messageType; }
	
	//-------------------------------------------------------------------------------------------------
	public GSDPollResponseDTO getGSDPollResponse() {
		if (payload instanceof GSDPollResponseDTO) {
			return (GSDPollResponseDTO) payload;
		}
		
		throw new DataNotFoundException("The response is not a result of a GSD poll.");
	}
	
	//-------------------------------------------------------------------------------------------------
	public ICNProposalResponseDTO getICNProposalResponse() {
		if (payload instanceof ICNProposalResponseDTO) {
			return (ICNProposalResponseDTO) payload;
		}
		
		throw new DataNotFoundException("The response is not a reslult of an ICN proposal.");
	}
}