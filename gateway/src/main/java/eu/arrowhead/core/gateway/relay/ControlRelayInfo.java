package eu.arrowhead.core.gateway.relay;

import org.springframework.util.Assert;

import javax.jms.MessageProducer;

public class ControlRelayInfo {

	//=================================================================================================
	// members
	
	private final MessageProducer controlRequestMessageSender;
	private final MessageProducer controlResponseMessageSender;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public ControlRelayInfo(final MessageProducer controlRequestMessageSender, final MessageProducer controlResponseMessageSender) {
		Assert.notNull(controlRequestMessageSender, "controlRequestMessageSender is null.");
		Assert.notNull(controlResponseMessageSender, "controlResponseMessageSender is null.");
		
		this.controlRequestMessageSender = controlRequestMessageSender;
		this.controlResponseMessageSender = controlResponseMessageSender;
	}

	//-------------------------------------------------------------------------------------------------
	public MessageProducer getControlRequestMessageSender() { return controlRequestMessageSender; }
	public MessageProducer getControlResponseMessageSender() { return controlResponseMessageSender; }
}