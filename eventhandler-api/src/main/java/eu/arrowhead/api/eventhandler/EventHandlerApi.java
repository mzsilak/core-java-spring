package eu.arrowhead.api.eventhandler;

import eu.arrowhead.api.ApiMethod;
import eu.arrowhead.api.annotations.ArrowheadApi;
import eu.arrowhead.api.annotations.ArrowheadService;
import eu.arrowhead.api.annotations.LookupSource;
import eu.arrowhead.api.annotations.OptionParam;
import eu.arrowhead.api.annotations.PayloadParam;
import eu.arrowhead.api.eventhandler.model.EventPublishRequestDTO;
import eu.arrowhead.api.eventhandler.model.SubscriptionRequestDTO;
import eu.arrowhead.common.CommonConstants;

import static eu.arrowhead.common.CommonConstants.CORE_SERVICE_EVENT_HANDLER_PUBLISH;
import static eu.arrowhead.common.CommonConstants.CORE_SERVICE_EVENT_HANDLER_PUBLISH_AUTH_UPDATE;
import static eu.arrowhead.common.CommonConstants.CORE_SERVICE_EVENT_HANDLER_SUBSCRIBE;
import static eu.arrowhead.common.CommonConstants.CORE_SERVICE_EVENT_HANDLER_UNSUBSCRIBE;

@ArrowheadApi(lookupSource = LookupSource.Source.SERVICE_REGISTRY)
public interface EventHandlerApi {

    @ArrowheadService(serviceDef = CORE_SERVICE_EVENT_HANDLER_SUBSCRIBE, method = ApiMethod.CREATE)
    void subscriber(@PayloadParam final SubscriptionRequestDTO request);

    @ArrowheadService(serviceDef = CORE_SERVICE_EVENT_HANDLER_UNSUBSCRIBE, method = ApiMethod.DELETE)
    void unsubscribe(@OptionParam(CommonConstants.OP_EVENT_HANDLER_UNSUBSCRIBE_REQUEST_PARAM_EVENT_TYPE) final String eventType,
                     @OptionParam(CommonConstants.OP_EVENT_HANDLER_UNSUBSCRIBE_REQUEST_PARAM_SUBSCRIBER_SYSTEM_NAME) final String subscriberName,
                     @OptionParam(CommonConstants.OP_EVENT_HANDLER_UNSUBSCRIBE_REQUEST_PARAM_SUBSCRIBER_ADDRESS) final String subscriberAddress,
                     @OptionParam(CommonConstants.OP_EVENT_HANDLER_UNSUBSCRIBE_REQUEST_PARAM_SUBSCRIBER_PORT) final int subscriberPort);

    @ArrowheadService(serviceDef = CORE_SERVICE_EVENT_HANDLER_PUBLISH, method = ApiMethod.CREATE)
    void publish(@PayloadParam final EventPublishRequestDTO request);

    @ArrowheadService(serviceDef = CORE_SERVICE_EVENT_HANDLER_PUBLISH_AUTH_UPDATE, method = ApiMethod.CREATE)
    void publishSubscriberAuthorizationUpdate(@PayloadParam final EventPublishRequestDTO request);
}
