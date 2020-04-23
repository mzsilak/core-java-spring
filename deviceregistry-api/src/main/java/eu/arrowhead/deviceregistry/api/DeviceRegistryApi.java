package eu.arrowhead.deviceregistry.api;


import eu.arrowhead.common.api.ApiMethod;
import eu.arrowhead.common.api.Protocol;
import eu.arrowhead.common.api.annotations.ArrowheadApi;
import eu.arrowhead.common.api.annotations.ArrowheadService;
import eu.arrowhead.common.api.annotations.LookupPolicy;
import eu.arrowhead.common.api.annotations.OptionParam;
import eu.arrowhead.common.api.exception.ArrowheadException;
import eu.arrowhead.deviceregistry.api.model.DeviceRegistryRequestDTO;
import eu.arrowhead.deviceregistry.api.model.DeviceRegistryResponseDTO;

import static eu.arrowhead.common.CommonConstants.CORE_SERVICE_DEVICE_REGISTRY_REGISTER;
import static eu.arrowhead.common.CommonConstants.CORE_SERVICE_DEVICE_REGISTRY_UNREGISTER;
import static eu.arrowhead.common.api.annotations.LookupPolicy.Policy.ALWAYS;
import static eu.arrowhead.common.api.annotations.LookupPolicy.Policy.ON_ERROR;
import static eu.arrowhead.common.api.annotations.LookupSource.Source.ALL;

@ArrowheadApi(lookupPolicy = ON_ERROR, lookupSource = ALL, protocols = Protocol.HTTP)
public interface DeviceRegistryApi {

    @LookupPolicy(ALWAYS)
    @ArrowheadService(serviceDef = CORE_SERVICE_DEVICE_REGISTRY_REGISTER, method = ApiMethod.CREATE)
    DeviceRegistryResponseDTO registerDevice(final DeviceRegistryRequestDTO request) throws ArrowheadException;

    @ArrowheadService(serviceDef = CORE_SERVICE_DEVICE_REGISTRY_UNREGISTER, method = ApiMethod.CREATE)
    void unregisterDevice(@OptionParam("device_name") final String deviceName,
                          @OptionParam("mad_address") final String macAddress)
            throws ArrowheadException;
}
