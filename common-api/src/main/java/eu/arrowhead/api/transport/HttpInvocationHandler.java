package eu.arrowhead.api.transport;

import eu.arrowhead.api.Encoding;
import eu.arrowhead.api.Protocol;
import eu.arrowhead.api.annotations.ArrowheadApi;
import eu.arrowhead.api.annotations.ArrowheadService;
import eu.arrowhead.api.annotations.LookupPolicy;
import eu.arrowhead.api.annotations.LookupSource;
import eu.arrowhead.api.common.exception.ArrowheadException;
import eu.arrowhead.api.common.exception.AuthException;
import eu.arrowhead.api.common.exception.DataNotFoundException;
import eu.arrowhead.api.common.exception.ExceptionType;
import eu.arrowhead.api.common.exception.UnavailableServerException;
import eu.arrowhead.api.common.model.ErrorMessageDTO;
import eu.arrowhead.api.orchestration.model.OrchestrationFlags;
import eu.arrowhead.api.orchestration.model.OrchestrationFormRequestDTO;
import eu.arrowhead.api.orchestration.model.OrchestrationResponseDTO;
import eu.arrowhead.api.orchestration.model.OrchestrationResultDTO;
import eu.arrowhead.api.serviceregistry.model.ServiceQueryFormDTO;
import eu.arrowhead.api.serviceregistry.model.ServiceQueryResultDTO;
import eu.arrowhead.api.serviceregistry.model.ServiceRegistryResponseDTO;
import eu.arrowhead.api.systemregistry.model.ServiceSecurityType;
import eu.arrowhead.api.systemregistry.model.SystemRequestDTO;
import eu.arrowhead.api.systemregistry.model.SystemResponseDTO;
import eu.arrowhead.common.ClientSSLProperties;
import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.Utilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.WWWAuthenticationProtocolHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.JettyClientHttpConnector;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static eu.arrowhead.common.CommonConstants.CORE_SERVICE_ORCH_PROCESS;

public class HttpInvocationHandler implements InvocationHandler {

    private static final String SERVICE_REGISTRY_SERVICE_DEFINITION = "service-query";
    private static final String ORCHESTRATION_SERVICE_DEFINITION = CORE_SERVICE_ORCH_PROCESS;
    private static final String ERROR_MESSAGE_PART_PKIX_PATH = "PKIX path";
    private static final String ERROR_MESSAGE_PART_SUBJECT_ALTERNATIVE_NAMES = "doesn't match any of the subject alternative names";

    private final Logger logger = LogManager.getLogger();
    private final Protocol protocol = Protocol.HTTP;
    private final Predicate<HttpStatus> errorPredicate = createErrorPredicate();

    private final TransportCache<UriComponents> serviceCache;
    private final UriComponents srUri;
    private final ClientSSLProperties sslProperties;
    private final WebClient webClient;
    private final SystemRequestDTO requester;

    private final HttpClient httpClient;

    public HttpInvocationHandler(final SystemRequestDTO requester, final UriComponents srUri) throws Exception {
        this(requester, srUri, new TransportCache<>(), new ClientSSLProperties());
    }

    public HttpInvocationHandler(final SystemRequestDTO requester, final UriComponents srUri, final ClientSSLProperties sslProperties) throws Exception {
        this(requester, srUri, new TransportCache<>(), sslProperties);
    }

    public HttpInvocationHandler(final SystemRequestDTO requester, final UriComponents srUri, final TransportCache<UriComponents> cache) throws Exception {
        this(requester, srUri, cache, new ClientSSLProperties());
    }

    public HttpInvocationHandler(final SystemRequestDTO requester, final UriComponents srUri,
                                 final TransportCache<UriComponents> serviceCache, final ClientSSLProperties sslProperties)
            throws Exception {
        this.requester = requester;
        this.serviceCache = Objects.requireNonNull(serviceCache, "TransportCache must not be null");
        this.srUri = Objects.requireNonNull(srUri, "ServiceRegistry URI must not be null");
        this.sslProperties = Objects.requireNonNull(sslProperties, "SSLProperties must not be null");

        serviceCache.put(SERVICE_REGISTRY_SERVICE_DEFINITION, srUri);

        final SslContextFactory sslFactory = createSslContextFactory(sslProperties);
        final HttpLogger httpLogger = new HttpLogger(logger);

        httpClient = new HttpClient(sslFactory);
        httpClient.start();

        httpClient.getProtocolHandlers().remove(WWWAuthenticationProtocolHandler.NAME);

        webClient = WebClient.builder()
                             .clientConnector(new JettyClientHttpConnector(httpClient))
                             .filter(httpLogger.logRequest())
                             .build();

    }

    @PreDestroy
    public void stop() throws Exception {
        httpClient.stop();
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) {
        final Class<?> declaringClass = method.getDeclaringClass();
        final ArrowheadApi api = declaringClass.getAnnotation(ArrowheadApi.class);
        logger.debug("Invoking {}.{}({})", declaringClass.getSimpleName(), method.getName(), args);

        checkSupported(api, declaringClass.getSimpleName());
        if (isCommonMethod(method)) {
            return handleCommonMethod(proxy, method, args);
        }

        final ArrowheadService service = method.getAnnotation(ArrowheadService.class);
        final LookupPolicy.Policy policy = findPolicy(api, method);
        final LookupSource.Source source = findSource(api, method);
        final String[] interfaces = mapInterfaces(api);

        final UriComponents uriTemplate = findUri(policy, source, service.serviceDef(), interfaces);
        final HttpMethod httpMethod = mapMethod(service);
        final HttpParameters parameters = HttpParameters.from(method.getParameterAnnotations(), args);
        final Class<?> returnType = method.getReturnType();

        final UriComponents serviceUri = parameters.adaptUri(uriTemplate);
        final Consumer<HttpHeaders> headersConsumer = parameters.getHeadersConsumer();

        if (HttpMethod.GET.equals(httpMethod) || HttpMethod.DELETE.equals(httpMethod)) {
            return exchange(httpMethod, serviceUri.toUriString(), returnType, headersConsumer);
        } else {
            return exchange(httpMethod, serviceUri.toUriString(), returnType, headersConsumer, parameters.getPayload());
        }
    }

    private Object handleCommonMethod(final Object proxy, final Method method, final Object[] args) {
        if (ReflectionUtils.isToStringMethod(method)) {
            return method.getDeclaringClass().getSimpleName() + ".proxy";
        } else if (ReflectionUtils.isEqualsMethod(method)) {
            return (args.length == 1) && Objects.equals(proxy, args[0]);
        } else if (ReflectionUtils.isHashCodeMethod(method)) {
            return Objects.hash(proxy, this);
        } else {
            return null;
        }
    }

    private boolean isCommonMethod(final Method method) {
        return ReflectionUtils.isObjectMethod(method);
    }

    private UriComponents findUri(final LookupPolicy.Policy policy, final LookupSource.Source source,
                                  final String serviceDef, final String[] interfaces) {

        logger.debug("Finding uri for '{}' with policy {} and source {}", serviceDef, policy, source);
        final Optional<UriComponents> cacheUri = findUriByContext(serviceDef);
        Optional<UriComponents> returnValue = cacheUri;

        switch (source) {
            case CACHE:
                // we did a cache lookup already; nothing to do here
                break;
            case SERVICE_REGISTRY:
                if (cacheUri.isEmpty() || LookupPolicy.Policy.ALWAYS.equals(policy)) {
                    returnValue = Optional.of(doFindUriByServiceRegistry(serviceDef, interfaces));
                }
                break;
            case ORCHESTRATION_SERVICE:
                if (cacheUri.isEmpty() || LookupPolicy.Policy.ALWAYS.equals(policy)) {
                    returnValue = Optional.of(doFindUriByOrchestrationService(serviceDef, interfaces));
                }
                break;
            case ALL:
            default:
                Optional<UriComponents> searchUri = cacheUri;

                if (cacheUri.isEmpty() || LookupPolicy.Policy.ALWAYS.equals(policy)) {
                    searchUri = findUriByOrchestrationService(serviceDef, interfaces);
                }

                if (searchUri.isEmpty()) {
                    returnValue = Optional.of(doFindUriByServiceRegistry(serviceDef, interfaces));
                }
                break;
        }

        if(returnValue.isEmpty()) {
            logger.debug("Failed to find uri for '{}'", serviceDef);
            throw new UnavailableServerException("The service behind cache uri '" + serviceDef + "' is not reachable");
        } else {
            logger.debug("Found reachable uri for '{}': {}", serviceDef, returnValue.get());
            return returnValue.get();
        }
    }

    private Optional<UriComponents> findUriByContext(final String serviceDef) {
        final Optional<UriComponents> cacheUri = serviceCache.get(serviceDef);
        if (cacheUri.isPresent() && reachable(cacheUri.get())) {
            return cacheUri;
        }
        return Optional.empty();
    }

    /*
    private Optional<UriComponents> findUriByServiceRegistry(final String serviceDef,
                                                             final String[] interfaces) {
        try {
            return Optional.of(doFindUriByServiceRegistry(serviceDef, interfaces));
        } catch (final ArrowheadException ex) {
            return Optional.empty();
        }
    }
     */

    private Optional<UriComponents> findUriByOrchestrationService(final String serviceDef,
                                                                  final String[] interfaces) {
        try {
            return Optional.of(doFindUriByOrchestrationService(serviceDef, interfaces));
        } catch (final ArrowheadException ex) {
            return Optional.empty();
        }
    }

    private UriComponents doFindUriByServiceRegistry(final String serviceDef, final String[] interfaces) {
        final ServiceQueryFormDTO form = new ServiceQueryFormDTO.Builder(serviceDef)
                .interfaces(interfaces)
                .build();

        final ServiceQueryResultDTO result = exchange(HttpMethod.POST, srUri.toUriString(), ServiceQueryResultDTO.class, new HttpHeadersConsumer(), form);
        return pickFromResult(result.getServiceQueryData(), "ServiceRegistry", serviceDef);
    }

    private UriComponents doFindUriByOrchestrationService(final String serviceDef, final String[] interfaces) {

        final UriComponents orchUri = findUri(LookupPolicy.Policy.ON_ERROR, LookupSource.Source.SERVICE_REGISTRY,
                                              ORCHESTRATION_SERVICE_DEFINITION, interfaces);

        final ServiceQueryFormDTO form = new ServiceQueryFormDTO.Builder(serviceDef)
                .interfaces(interfaces)
                .build();
        final OrchestrationFormRequestDTO orchestrationForm = new OrchestrationFormRequestDTO
                .Builder(requester)
                .requestedService(form)
                .flag(OrchestrationFlags.Flag.OVERRIDE_STORE, true)
                .build();

        final OrchestrationResponseDTO result = exchange(HttpMethod.POST, orchUri.toUriString(), OrchestrationResponseDTO.class,
                                                         new HttpHeadersConsumer(), orchestrationForm);
        return pickFromResult(result.getResponse(), "OrchestrationService", serviceDef);
    }

    private <T> UriComponents pickFromResult(final List<T> list, final String source, final String serviceDef) {

        if (Objects.isNull(list) || list.isEmpty()) {
            throw new DataNotFoundException("Unable to find " + serviceDef + " using " + source);
        }

        for (int reverseIndex = list.size() - 1; reverseIndex >= 0; reverseIndex--) {
            final T entry = list.get(reverseIndex);
            final UriComponents uri = createUri(entry);

            if (reachable(uri)) {
                serviceCache.put(serviceDef, uri);
                return uri;
            }
        }
        throw new UnavailableServerException("The service " + serviceDef + " is not reachable", HttpStatus.NOT_FOUND.value());
    }

    private <T> T exchange(final HttpMethod method, final String uriString, final Class<T> returnClass,
                           final Consumer<HttpHeaders> headers, final Object body) {
        try {
            return webClient.method(method)
                            .uri(uriString)
                            .headers(headers)
                            .syncBody(body)
                            .retrieve()
                            .onStatus(errorPredicate, r -> mapException(r, uriString))
                            .bodyToMono(returnClass)
                            .block();
        } catch (final Throwable ex) {
            throw convertException(ex, uriString);
        }
    }

    private <T> T exchange(final HttpMethod method, final String uriString, final Class<T> returnClass, final Consumer<HttpHeaders> headers) {
        try {
            return webClient.method(method)
                            .uri(uriString)
                            .headers(headers)
                            .retrieve()
                            .onStatus(errorPredicate, r -> mapException(r, uriString))
                            .bodyToMono(returnClass)
                            .block();
        } catch (final Throwable ex) {
            throw convertException(ex, uriString);
        }
    }

    private ArrowheadException convertException(final Throwable ex, final String uriString) {
        if (ArrowheadException.class.isAssignableFrom(ex.getClass())) {
            throw (ArrowheadException) ex;
        } else if (ex.getMessage().contains(ERROR_MESSAGE_PART_PKIX_PATH)) {
            logger.error("The system at {} is not part of the same certificate chain of trust!", uriString);
            return new AuthException("The system at " + uriString + " is not part of the same certificate chain of trust!",
                                     HttpStatus.UNAUTHORIZED.value(), ex);
        } else if (ex.getMessage().contains(ERROR_MESSAGE_PART_SUBJECT_ALTERNATIVE_NAMES)) {
            final String error = String.format("The certificate of the system at %s does not contain " +
                                                       "the specified IP address or DNS name as a Subject Alternative Name.", uriString);
            logger.error(error);
            return new AuthException(error, HttpStatus.UNAUTHORIZED.value(), ex);
        } else {
            logger.error("Unknown exception '{}': {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
            return new ArrowheadException(ex.getMessage(), ex);
        }
    }

    private boolean reachable(final UriComponents cacheUri) {
        final UriComponents echoUri = createEchoUri(cacheUri);
        try {
            logger.trace("pinging {}", echoUri);
            exchange(HttpMethod.GET, echoUri.toUriString(), String.class, new HttpHeadersConsumer());
            return true;
        } catch (final ArrowheadException ex) {
            logger.warn("Ping to {} failed with '{}': {}", echoUri.toUriString(), ex.getClass().getSimpleName(), ex.getMessage());
            return false;
        }
    }

    private Mono<? extends Throwable> mapException(final ClientResponse clientResponse, final String uriString) {
        return clientResponse.bodyToMono(ErrorMessageDTO.class)
                             .onErrorResume(ex -> createUnknownErrorDto(ex, clientResponse, uriString))
                             .flatMap(dto -> dtoToExceptionTransformer(dto, clientResponse, uriString));
    }

    private Mono<? extends Throwable> dtoToExceptionTransformer(final ErrorMessageDTO dto,
                                                                final ClientResponse clientResponse,
                                                                final String uriString) {

        final HttpStatus httpStatus = clientResponse.statusCode();
        final String origin = Objects.nonNull(dto.getOrigin()) ? dto.getOrigin() : uriString;
        final int statusCode = dto.getErrorCode() > 0 ? dto.getErrorCode() : httpStatus.value();
        final String reason = Objects.nonNull(dto.getErrorMessage()) ? dto.getErrorMessage() : httpStatus.getReasonPhrase();

        logger.error("Request failed at '{}' with status {}, error message: {}", origin, statusCode, reason);

        if (Objects.isNull(dto.getExceptionType())) {
            return Mono.error(new ArrowheadException("Unknown error occurred: " + httpStatus.getReasonPhrase(), httpStatus.value()));
        } else {
            return Mono.error(Utilities.createExceptionFromErrorMessageDTO(dto));
        }
    }

    private Mono<? extends ErrorMessageDTO> createUnknownErrorDto(final Throwable ex,
                                                                  final ClientResponse clientResponse,
                                                                  final String uriString) {
        logger.debug("Unable to deserialize error message: {}", ex.getMessage(), ex);
        final ErrorMessageDTO dto = new ErrorMessageDTO();
        dto.setErrorMessage(ex.getMessage());
        dto.setErrorCode(clientResponse.rawStatusCode());
        dto.setExceptionType(ExceptionType.GENERIC);
        dto.setOrigin(uriString);

        return Mono.just(dto);
    }

    private Predicate<HttpStatus> createErrorPredicate() {
        return (status) -> !status.is2xxSuccessful();
    }

    private UriComponents createEchoUri(final UriComponents serviceUri) {
        Objects.requireNonNull(serviceUri, "ServiceUri must not be null");
        final List<String> pathSegments = serviceUri.getPathSegments();
        final String systemPath = pathSegments.isEmpty() ? serviceUri.getPath() : pathSegments.get(0);
        return UriComponentsBuilder.newInstance()
                                   .scheme(serviceUri.getScheme())
                                   .host(serviceUri.getHost())
                                   .port(serviceUri.getPort())
                                   .path(systemPath)
                                   .path(CommonConstants.ECHO_URI)
                                   .build();
    }

    private <T> UriComponents createUri(final T entry) {
        if (entry instanceof ServiceRegistryResponseDTO) {
            return createUri((ServiceRegistryResponseDTO) entry);
        } else if (entry instanceof OrchestrationResultDTO) {
            return createUri((OrchestrationResultDTO) entry);
        } else {
            throw new ArrowheadException("Unable to extract uri from list entry: " + entry.getClass());
        }
    }

    private UriComponents createUri(final ServiceRegistryResponseDTO entry) {
        return createUri(entry.getSecure(), entry.getProvider(), entry.getServiceUri());
    }

    private UriComponents createUri(final OrchestrationResultDTO entry) {
        return createUri(entry.getSecure(), entry.getProvider(), entry.getServiceUri());
    }

    private UriComponents createUri(final ServiceSecurityType securityType, final SystemResponseDTO system, final String serviceUri) {
        return Utilities.createURI(getScheme(securityType),
                                   system.getAddress(),
                                   system.getPort(),
                                   serviceUri);
    }

    private String getScheme(final ServiceSecurityType securityType) {
        return ServiceSecurityType.NOT_SECURE.equals(securityType) ? CommonConstants.HTTP : CommonConstants.HTTPS;
    }

    private LookupPolicy.Policy findPolicy(final ArrowheadApi api, final Method method) {
        final LookupPolicy lookupPolicy = method.getAnnotation(LookupPolicy.class);
        return Objects.nonNull(lookupPolicy) ? lookupPolicy.value() : api.lookupPolicy();
    }

    private LookupSource.Source findSource(final ArrowheadApi api, final Method method) {
        final LookupSource lookupSource = method.getAnnotation(LookupSource.class);
        return Objects.nonNull(lookupSource) ? lookupSource.value() : api.lookupSource();
    }

    private String[] mapInterfaces(final ArrowheadApi api) {
        return mapInterfaces(protocol, api.encoding());
    }

    private HttpMethod mapMethod(final ArrowheadService service) {
        switch (service.method()) {
            case CREATE: return HttpMethod.POST;
            case READ: return HttpMethod.GET;
            case UPDATE: return HttpMethod.PUT;
            case MERGE: return HttpMethod.PATCH;
            case DELETE: return HttpMethod.DELETE;
            default: throw new UnsupportedOperationException("Unsupported ApiMethod: " + service.method());
        }
    }

    private String[] mapInterfaces(final Protocol protocol, final Encoding[] encodings) {
        final String[] retValue = new String[encodings.length];
        for (int i = 0; i < retValue.length; i++) {
            retValue[i] = encodings[i].getInterface(protocol, sslProperties.isSslEnabled());
        }
        return retValue;
    }

    private void checkSupported(final ArrowheadApi api, final String clsName) {
        boolean found = false;
        for (Protocol p : api.protocols()) {
            found |= protocol.equals(p);
        }
        if (!found) {
            throw new UnsupportedProtocolException(clsName, protocol);
        }
    }

    private SslContextFactory createSslContextFactory(final ClientSSLProperties sslProperties) throws IOException {
        final SslContextFactory sslFactory = new SslContextFactory.Client();
        if (Objects.nonNull(sslProperties.getKeyStore())) {
            final File keyStore = sslProperties.getKeyStore().getFile();
            sslFactory.setKeyStorePath(keyStore.getAbsolutePath());
            sslFactory.setKeyStorePassword(sslProperties.getKeyStorePassword());
            sslFactory.setKeyStoreType(sslProperties.getKeyStoreType());
            sslFactory.setKeyManagerPassword(sslProperties.getKeyPassword());
        }
        if (Objects.nonNull(sslProperties.getTrustStore())) {
            final File trustStore = sslProperties.getTrustStore().getFile();
            sslFactory.setTrustStorePath(trustStore.getAbsolutePath());
            sslFactory.setTrustStorePassword(sslProperties.getTrustStorePassword());
            sslFactory.setTrustStoreType(sslProperties.getKeyStoreType());
        }
        return sslFactory;
    }
}
