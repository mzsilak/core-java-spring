package eu.arrowhead.common.transport;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.arrowhead.common.ClientSSLProperties;
import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.api.Protocol;
import eu.arrowhead.common.api.annotations.ArrowheadApi;
import eu.arrowhead.common.api.annotations.ArrowheadService;
import eu.arrowhead.common.api.annotations.LookupPolicy;
import eu.arrowhead.common.api.annotations.LookupSource;
import eu.arrowhead.common.api.exception.ArrowheadException;
import eu.arrowhead.common.api.exception.AuthException;
import eu.arrowhead.common.api.exception.ExceptionType;
import eu.arrowhead.common.api.exception.UnavailableServerException;
import eu.arrowhead.common.api.model.ErrorMessageDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.JettyClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class HttpInvocationHandler implements InvocationHandler {

    private static final String ERROR_MESSAGE_PART_PKIX_PATH = "PKIX path";
    private static final String ERROR_MESSAGE_PART_SUBJECT_ALTERNATIVE_NAMES = "doesn't match any of the subject alternative names";

    private final Logger logger = LogManager.getLogger();
    private final Protocol protocol = Protocol.HTTP;
    private final Predicate<HttpStatus> errorPredicate = createErrorPredicate();
    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final TransportCache<UriComponents> serviceCache;
    private final UriComponents srUri;
    private final ClientSSLProperties sslProperties;
    private final WebClient webClient;

    public HttpInvocationHandler(final UriComponents srUri) throws IOException {
        this(srUri, new TransportCache<>(), new ClientSSLProperties());
    }

    public HttpInvocationHandler(final UriComponents srUri, final ClientSSLProperties sslProperties) throws IOException {
        this(srUri, new TransportCache<>(), sslProperties);
    }

    public HttpInvocationHandler(final UriComponents srUri, final TransportCache<UriComponents> cache) throws IOException {
        this(srUri, cache, new ClientSSLProperties());
    }

    public HttpInvocationHandler(final UriComponents srUri, final TransportCache<UriComponents> serviceCache, final ClientSSLProperties sslProperties)
            throws IOException {
        this.serviceCache = Objects.requireNonNull(serviceCache, "TransportCache must not be null");
        this.srUri = Objects.requireNonNull(srUri, "ServiceRegistry URI must not be null");
        this.sslProperties = Objects.requireNonNull(sslProperties, "SSLProperties must not be null");

        final SslContextFactory sslFactory = create(sslProperties);
        final JettyClientHttpConnector jettyConnector = new JettyClientHttpConnector(new HttpClient(sslFactory));

        webClient = WebClient.builder().clientConnector(jettyConnector).build();
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        final Class<?> declaringClass = method.getDeclaringClass();
        final ArrowheadApi api = declaringClass.getAnnotation(ArrowheadApi.class);
        final ArrowheadService service = method.getAnnotation(ArrowheadService.class);

        checkSupported(api, declaringClass.getSimpleName());
        final LookupPolicy.Policy policy = findPolicy(api, method);
        final LookupSource.Source source = findSource(api, method);

        final UriComponents uri = findUri(policy, source, service.serviceDef());

        return null;
    }

    private UriComponents findUri(final LookupPolicy.Policy policy, final LookupSource.Source source, final String serviceDef) {

        final Optional<UriComponents> cacheUri = findUriByContext(serviceDef);
        final Optional<UriComponents> retValue;

        switch (source) {
            case CACHE:
                retValue = cacheUri;
                break;
            case SERVICE_REGISTRY:
                retValue = findUriByServiceRegistry(policy, serviceDef, cacheUri);
                break;
            case ORCHESTRATION_SERVICE:
                retValue = findUriByOrchestrationService(policy, serviceDef, cacheUri);
                break;
            case ALL:
            default:
                retValue = cacheUri.or(() -> findUriByOrchestrationService(policy, serviceDef, cacheUri))
                                   .or(() -> findUriByServiceRegistry(policy, serviceDef, cacheUri));
        }

        return retValue.orElseThrow(() -> new UnavailableServerException("The service is not reachable", HttpStatus.NOT_FOUND.value()));
    }

    private Optional<UriComponents> findUriByContext(final String serviceDef) {
        final Optional<UriComponents> cacheUri = serviceCache.get(serviceDef);
        if (cacheUri.isPresent() && ping(cacheUri.get())) {
            return cacheUri;
        }
        return Optional.empty();
    }


    private Optional<UriComponents> findUriByOrchestrationService(final LookupPolicy.Policy policy,
                                                                  final String serviceDef,
                                                                  final Optional<UriComponents> cacheUri) {
        return Optional.empty();
    }

    private Optional<UriComponents> findUriByServiceRegistry(final LookupPolicy.Policy policy,
                                                             final String serviceDef,
                                                             final Optional<UriComponents> cacheUri) {
        return Optional.empty();
    }

    private boolean ping(final UriComponents cacheUri) {
        final UriComponents echoUri = createEchoUri(cacheUri);
        try {
            logger.trace("pinging {}", echoUri);
            webClient.get()
                     .uri(echoUri.toUri())
                     .retrieve()
                     .onStatus(errorPredicate, r -> mapException(r, echoUri))
                     .bodyToMono(String.class)
                     .block();

            return true;
        } catch (final Throwable ex) {
            if (ex.getMessage().contains(ERROR_MESSAGE_PART_PKIX_PATH)) {
                logger.error("The system at {} is not part of the same certificate chain of trust!", echoUri.toUriString());
                throw new AuthException("The system at " + echoUri.toUriString() + " is not part of the same certificate chain of trust!",
                                        HttpStatus.UNAUTHORIZED.value(), ex);
            } else if (ex.getMessage().contains(ERROR_MESSAGE_PART_SUBJECT_ALTERNATIVE_NAMES)) {
                logger.error("The certificate of the system at {} does not contain the specified IP address or DNS name as a Subject Alternative Name.",
                             echoUri.toString());
                throw new AuthException("The certificate of the system at " + echoUri
                        .toString() + " does not contain the specified IP address or DNS name as a Subject Alternative Name.");
            }
            logger.warn("Ping to {} failed with: {}", echoUri.toUriString(), ex.getMessage());
            return false;
        }
    }

    private Mono<? extends Throwable> mapException(final ClientResponse clientResponse, final UriComponents echoUri) {
        return clientResponse.bodyToMono(ErrorMessageDTO.class)
                             .onErrorResume(ex -> createUnknownException(ex, clientResponse, echoUri))
                             .flatMap(dto -> dtoToExceptionTransformer(dto, clientResponse, echoUri));
    }

    private Mono<? extends Throwable> dtoToExceptionTransformer(final ErrorMessageDTO dto,
                                                                final ClientResponse clientResponse,
                                                                final UriComponents echoUri) {

        final HttpStatus httpStatus = clientResponse.statusCode();
        if (Objects.isNull(dto.getExceptionType())) {
            logger.error("Request failed at '{}' with status {}, error message: {}", echoUri.toUriString(), httpStatus.value(), httpStatus.getReasonPhrase());
            return Mono.error(new ArrowheadException("Unknown error occurred: " + httpStatus.getReasonPhrase(), httpStatus.value()));
        }

        logger.error("Request failed at '{}' with status {}, error message: {}", dto.getOrigin(), dto.getErrorCode(), dto.getErrorMessage());
        return Mono.error(Utilities.createExceptionFromErrorMessageDTO(dto));
    }

    private Mono<? extends ErrorMessageDTO> createUnknownException(final Throwable ex,
                                                                   final ClientResponse clientResponse,
                                                                   final UriComponents echoUri) {
        logger.debug("Unable to deserialize error message: {}", ex.getMessage(), ex);
        final ErrorMessageDTO dto = new ErrorMessageDTO();
        dto.setErrorMessage(ex.getMessage());
        dto.setErrorCode(clientResponse.rawStatusCode());
        dto.setExceptionType(ExceptionType.GENERIC);
        dto.setOrigin(echoUri.toUriString());

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

    private LookupPolicy.Policy findPolicy(final ArrowheadApi api, final Method method) {
        final LookupPolicy lookupPolicy = method.getAnnotation(LookupPolicy.class);
        return Objects.nonNull(lookupPolicy) ? lookupPolicy.value() : api.lookupPolicy();
    }

    private LookupSource.Source findSource(final ArrowheadApi api, final Method method) {
        final LookupSource lookupSource = method.getAnnotation(LookupSource.class);
        return Objects.nonNull(lookupSource) ? lookupSource.value() : api.lookupSource();
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

    private SslContextFactory create(final ClientSSLProperties sslProperties) throws IOException {
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
