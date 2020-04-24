package eu.arrowhead.core.eventhandler.publish;

import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.database.entity.Subscription;
import eu.arrowhead.common.dto.internal.DTOConverter;
import eu.arrowhead.api.eventhandler.model.EventPublishRequestDTO;
import eu.arrowhead.common.http.HttpService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponents;

public class PublishEventTask implements Runnable {

	//=================================================================================================
	// members
	
	private final Logger logger = LogManager.getLogger(PublishEventTask.class);
	
	private final Subscription subscription;
	private final EventPublishRequestDTO publishRequestDTO;
	private final HttpService httpService;

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------	
	public PublishEventTask(final Subscription subscription, final EventPublishRequestDTO publishRequestDTO, final HttpService httpService) {
		this.subscription = subscription;
		this.publishRequestDTO = publishRequestDTO;
		this.httpService = httpService;
	}

	//-------------------------------------------------------------------------------------------------	
	@Override
	public void run() {
		try {
			logger.debug("PublishEventTask.run started...");
			
			if (Thread.currentThread().isInterrupted()) {
				logger.trace("Thread {} is interrupted...", Thread.currentThread().getName());
				
				return;
			}
			
			validateMembers();
			final UriComponents subscriptionUri = getSubscriptionUri(subscription);
			httpService.sendRequest(subscriptionUri, HttpMethod.POST, Void.class, DTOConverter.convertEventPublishRequestDTOToEventDTO(publishRequestDTO));			
		} catch (final Throwable ex) {			
			logger.debug("Exception:", ex.getMessage());			
		}
	}
	
	//=================================================================================================
	// assistant methods
	
	//-------------------------------------------------------------------------------------------------
	private UriComponents getSubscriptionUri(final Subscription subscription) {
		logger.debug("getSubscriptionUri started...");
		
		final String scheme = Utilities.isEmpty(subscription.getSubscriberSystem().getAuthenticationInfo()) ? CommonConstants.HTTP : CommonConstants.HTTPS;

		return Utilities.createURI(scheme, subscription.getSubscriberSystem().getAddress(), subscription.getSubscriberSystem().getPort(), subscription.getNotifyUri());
	}
	
	//-------------------------------------------------------------------------------------------------
	private void validateMembers() {
		Assert.notNull(this.publishRequestDTO, "publishRequestDTO is null");
		Assert.notNull(this.subscription, "subscription is null");
		Assert.notNull(this.subscription.getSubscriberSystem(), "subscription.SubscriberSystem is null");
		Assert.notNull(this.httpService, "httpService is null");
	}
}