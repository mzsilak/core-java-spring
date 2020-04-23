package eu.arrowhead.deviceregistry.api;


import eu.arrowhead.common.api.ApiMethod;
import eu.arrowhead.common.api.Protocol;
import eu.arrowhead.common.api.annotations.ArrowheadApi;
import eu.arrowhead.common.api.annotations.ArrowheadService;
import eu.arrowhead.common.api.exception.ArrowheadException;
import eu.arrowhead.deviceregistry.api.model.DeviceRegistryOnboardingWithCsrRequestDTO;
import eu.arrowhead.deviceregistry.api.model.DeviceRegistryOnboardingWithCsrResponseDTO;
import eu.arrowhead.deviceregistry.api.model.DeviceRegistryOnboardingWithNameRequestDTO;
import eu.arrowhead.deviceregistry.api.model.DeviceRegistryOnboardingWithNameResponseDTO;

import static eu.arrowhead.common.CommonConstants.CORE_SERVICE_DEVICE_REGISTRY_ONBOARDING_WITH_CSR;
import static eu.arrowhead.common.CommonConstants.CORE_SERVICE_DEVICE_REGISTRY_ONBOARDING_WITH_NAME;
import static eu.arrowhead.common.api.annotations.LookupPolicy.Policy.ALWAYS;
import static eu.arrowhead.common.api.annotations.LookupSource.Source.CACHE;

@ArrowheadApi(lookupPolicy = ALWAYS, lookupSource = CACHE, protocols = Protocol.HTTP)
public interface DeviceRegistryOnboardingApi {

    @ArrowheadService(serviceDef = CORE_SERVICE_DEVICE_REGISTRY_ONBOARDING_WITH_NAME, method = ApiMethod.CREATE)
    DeviceRegistryOnboardingWithNameResponseDTO onboardDevice(final DeviceRegistryOnboardingWithNameRequestDTO request)
            throws ArrowheadException;

    @ArrowheadService(serviceDef = CORE_SERVICE_DEVICE_REGISTRY_ONBOARDING_WITH_CSR, method = ApiMethod.CREATE)
    DeviceRegistryOnboardingWithCsrResponseDTO onboardDevice(final DeviceRegistryOnboardingWithCsrRequestDTO request) throws ArrowheadException;

}
