package eu.arrowhead.api.deviceregistry;


import eu.arrowhead.api.ApiMethod;
import eu.arrowhead.api.Protocol;
import eu.arrowhead.api.annotations.ArrowheadApi;
import eu.arrowhead.api.annotations.ArrowheadService;
import eu.arrowhead.api.common.exception.ArrowheadException;
import eu.arrowhead.api.deviceregistry.model.DeviceRegistryOnboardingWithCsrRequestDTO;
import eu.arrowhead.api.deviceregistry.model.DeviceRegistryOnboardingWithCsrResponseDTO;
import eu.arrowhead.api.deviceregistry.model.DeviceRegistryOnboardingWithNameRequestDTO;
import eu.arrowhead.api.deviceregistry.model.DeviceRegistryOnboardingWithNameResponseDTO;

import static eu.arrowhead.api.annotations.LookupPolicy.Policy.ALWAYS;
import static eu.arrowhead.api.annotations.LookupSource.Source.CACHE;
import static eu.arrowhead.common.CommonConstants.CORE_SERVICE_DEVICE_REGISTRY_ONBOARDING_WITH_CSR;
import static eu.arrowhead.common.CommonConstants.CORE_SERVICE_DEVICE_REGISTRY_ONBOARDING_WITH_NAME;

@ArrowheadApi(lookupPolicy = ALWAYS, lookupSource = CACHE, protocols = Protocol.HTTP)
public interface DeviceRegistryOnboardingApi {

    @ArrowheadService(serviceDef = CORE_SERVICE_DEVICE_REGISTRY_ONBOARDING_WITH_NAME, method = ApiMethod.CREATE)
    DeviceRegistryOnboardingWithNameResponseDTO onboardDevice(final DeviceRegistryOnboardingWithNameRequestDTO request)
            throws ArrowheadException;

    @ArrowheadService(serviceDef = CORE_SERVICE_DEVICE_REGISTRY_ONBOARDING_WITH_CSR, method = ApiMethod.CREATE)
    DeviceRegistryOnboardingWithCsrResponseDTO onboardDevice(final DeviceRegistryOnboardingWithCsrRequestDTO request) throws ArrowheadException;

}
