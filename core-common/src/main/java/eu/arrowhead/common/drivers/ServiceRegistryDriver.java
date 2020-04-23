package eu.arrowhead.common.drivers;

import eu.arrowhead.api.serviceregistry.model.ServiceQueryFormDTO;
import eu.arrowhead.api.serviceregistry.model.ServiceQueryResultDTO;
import eu.arrowhead.api.serviceregistry.model.ServiceRegistryResponseDTO;
import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.core.CoreSystemService;
import eu.arrowhead.api.serviceregistry.model.ServiceRegistryRequestDTO;
import eu.arrowhead.common.http.HttpService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class ServiceRegistryDriver {

    private final Logger logger = LogManager.getLogger(ServiceRegistryDriver.class);
    private final DriverUtilities driverUtilities;
    private final HttpService httpService;

    @Autowired
    public ServiceRegistryDriver(final DriverUtilities driverUtilities, final HttpService httpService) {
        this.driverUtilities = driverUtilities;
        this.httpService = httpService;
    }

    public UriComponents findCoreSystemService(final CoreSystemService service) throws DriverUtilities.DriverException {
        logger.traceEntry("findCoreSystemService:{}", service);
        Assert.notNull(service, "CoreSystemService must not be null");
        return logger.traceExit(driverUtilities.findUriByServiceRegistry(service));
    }

    public ServiceQueryResultDTO queryRegistry(final ServiceQueryFormDTO form) {
        logger.traceEntry("queryRegistry: {}", form);
        Assert.notNull(form, "ServiceQueryFormDTO must not be null");

        final UriComponents uri = driverUtilities.getServiceRegistryQueryUri();
        final ResponseEntity<ServiceQueryResultDTO> httpEntity = httpService
                .sendRequest(uri, HttpMethod.POST, ServiceQueryResultDTO.class, form);
        return logger.traceExit(httpEntity.getBody());
    }

    public ServiceRegistryResponseDTO registerService(final ServiceRegistryRequestDTO request) throws DriverUtilities.DriverException {
        logger.traceEntry("registerService: {}", request);
        Assert.notNull(request, "ServiceRegistryRequestDTO must not be null");

        final UriComponents uri = driverUtilities.findUriByOrchestrator(CoreSystemService.SERVICE_REGISTRY_REGISTER_SERVICE);
        final ResponseEntity<ServiceRegistryResponseDTO> httpEntity = httpService
                .sendRequest(uri, HttpMethod.POST, ServiceRegistryResponseDTO.class, request);
        return logger.traceExit(httpEntity.getBody());
    }

    public void unregisterService(final String serviceDefinition, final String providerName,
                                  final String providerAddress, final int providerPort)
            throws DriverUtilities.DriverException {

        logger.traceEntry("unregisterService: {}, {}, {}, {}", serviceDefinition, providerName, providerAddress, providerPort);
        final UriComponents uri = driverUtilities.findUriByOrchestrator(CoreSystemService.SERVICE_REGISTRY_UNREGISTER_SERVICE);
        final MultiValueMap<String, String> queryMap = new LinkedMultiValueMap<>(4);
        queryMap.put(CommonConstants.OP_SERVICE_REGISTRY_UNREGISTER_REQUEST_PARAM_SERVICE_DEFINITION, List.of(serviceDefinition));
        queryMap.put(CommonConstants.OP_SERVICE_REGISTRY_UNREGISTER_REQUEST_PARAM_PROVIDER_SYSTEM_NAME, List.of(providerName));
        queryMap.put(CommonConstants.OP_SERVICE_REGISTRY_UNREGISTER_REQUEST_PARAM_PROVIDER_ADDRESS, List.of(providerAddress));
        queryMap.put(CommonConstants.OP_SERVICE_REGISTRY_UNREGISTER_REQUEST_PARAM_PROVIDER_PORT, List.of(String.valueOf(providerPort)));

        final UriComponents unregisterUri = UriComponentsBuilder.newInstance().uriComponents(uri)
                                                                .queryParams(queryMap).build();
        httpService.sendRequest(unregisterUri, HttpMethod.POST, Void.class);
        logger.traceExit();
    }
}
