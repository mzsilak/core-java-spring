package eu.arrowhead.api.deviceregistry;


import eu.arrowhead.api.ApiMethod;
import eu.arrowhead.api.Encoding;
import eu.arrowhead.api.Protocol;
import eu.arrowhead.api.annotations.ArrowheadApi;
import eu.arrowhead.api.annotations.ArrowheadService;
import eu.arrowhead.api.annotations.LookupPolicy;
import eu.arrowhead.api.annotations.OptionParam;
import eu.arrowhead.api.common.exception.ArrowheadException;
import eu.arrowhead.api.deviceregistry.model.DeviceRegistryRequestDTO;
import eu.arrowhead.api.deviceregistry.model.DeviceRegistryResponseDTO;

import static eu.arrowhead.api.annotations.LookupPolicy.Policy.ALWAYS;
import static eu.arrowhead.api.annotations.LookupPolicy.Policy.ON_ERROR;
import static eu.arrowhead.api.annotations.LookupSource.Source.ALL;
import static eu.arrowhead.common.CommonConstants.CORE_SERVICE_DEVICE_REGISTRY_REGISTER;
import static eu.arrowhead.common.CommonConstants.CORE_SERVICE_DEVICE_REGISTRY_UNREGISTER;

@ArrowheadApi(lookupPolicy = ON_ERROR, lookupSource = ALL, protocols = Protocol.HTTP, encoding = Encoding.JSON)
public interface DeviceRegistryApi {

    @ArrowheadService(serviceDef = CORE_SERVICE_DEVICE_REGISTRY_REGISTER, method = ApiMethod.CREATE)
    DeviceRegistryResponseDTO registerDevice(final DeviceRegistryRequestDTO request) throws ArrowheadException;

    @LookupPolicy(ALWAYS)
    @ArrowheadService(serviceDef = CORE_SERVICE_DEVICE_REGISTRY_UNREGISTER, method = ApiMethod.DELETE)
    void unregisterDevice(@OptionParam("device_name") final String deviceName,
                          @OptionParam("mad_address") final String macAddress)
            throws ArrowheadException;
}
