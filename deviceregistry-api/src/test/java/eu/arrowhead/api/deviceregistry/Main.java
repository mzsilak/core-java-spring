package eu.arrowhead.api.deviceregistry;

import eu.arrowhead.api.common.exception.ArrowheadException;
import eu.arrowhead.api.deviceregistry.model.DeviceRegistryRequestDTO;
import eu.arrowhead.api.systemregistry.model.SystemRequestDTO;
import eu.arrowhead.api.transport.HttpInvocationHandler;
import eu.arrowhead.api.transport.TransportCache;
import eu.arrowhead.common.ClientSSLProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Objects;

import static eu.arrowhead.common.CommonConstants.CORE_SERVICE_DEVICE_REGISTRY_REGISTER;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) throws Exception {

        LOGGER.info("Starting Prototype");
        HttpInvocationHandler invocationHandler = null;

        try {
            // requester system name and certificate do not match
            invocationHandler = withInvalidRequester();

            DeviceRegistryApi instance = createWith(invocationHandler);
            instance.registerDevice(new DeviceRegistryRequestDTO());
        } catch (ArrowheadException e) {
            LOGGER.fatal("{}: {}", e.getClass().getSimpleName(), e.getMessage());
        } finally {
            if(Objects.nonNull(invocationHandler)) invocationHandler.stop();
        }

        try {
            // requester system is not authorized to use 'device-register'
            invocationHandler = withValidRequester();

            DeviceRegistryApi instance = createWith(invocationHandler);
            instance.registerDevice(new DeviceRegistryRequestDTO());
        } catch (ArrowheadException e) {
            LOGGER.fatal("{}: {}", e.getClass().getSimpleName(), e.getMessage());
        } finally {
            if(Objects.nonNull(invocationHandler)) invocationHandler.stop();
        }

        try {
            // 'device-register' uri is available, but the request is invalid
            // anyway, it reached the target service and dynamically converted its parameters
            invocationHandler = withCachedUri();

            DeviceRegistryApi instance = createWith(invocationHandler);
            instance.registerDevice(new DeviceRegistryRequestDTO());
        } catch (ArrowheadException e) {
            LOGGER.fatal("{}: {}", e.getClass().getSimpleName(), e.getMessage());
        } finally {
            if(Objects.nonNull(invocationHandler)) invocationHandler.stop();
        }

        LOGGER.info("Finished Prototype");
    }

    private static DeviceRegistryApi createWith(final InvocationHandler invocationHandler) {
        return (DeviceRegistryApi) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                                                          new Class[]{DeviceRegistryApi.class},
                                                          invocationHandler);
    }

    private static HttpInvocationHandler withInvalidRequester() throws Exception {
        LOGGER.info("INVALID REQUESTER");
        return new HttpInvocationHandler(invalidSystemRequester(),
                                         createSrUri(),
                                         createTransportCache(),
                                         createSSLProperties());
    }

    private static HttpInvocationHandler withValidRequester() throws Exception {
        LOGGER.info("VALID REQUESTER");
        return new HttpInvocationHandler(validSystemRequester(),
                                         createSrUri(),
                                         createTransportCache(),
                                         createSSLProperties());
    }

    private static HttpInvocationHandler withCachedUri() throws Exception {
        LOGGER.info("CACHED URI");
        final TransportCache<UriComponents> transportCache = createTransportCache();
        transportCache.put(CORE_SERVICE_DEVICE_REGISTRY_REGISTER, UriComponentsBuilder.fromHttpUrl("https://127.0.0.1:8439/deviceregistry/register").build());

        return new HttpInvocationHandler(validSystemRequester(),
                                         createSrUri(),
                                         transportCache,
                                         createSSLProperties());
    }

    private static SystemRequestDTO invalidSystemRequester() {
        return new SystemRequestDTO("name", "address", 1234, null);
    }

    private static SystemRequestDTO validSystemRequester() {
        return new SystemRequestDTO("sysop", "127.0.0.1", 1234, null);
    }

    private static UriComponents createSrUri() {
        return UriComponentsBuilder.fromUriString("https://127.0.0.1:8443/serviceregistry/query").build();
    }

    private static TransportCache<UriComponents> createTransportCache() {
        return new TransportCache<>();
    }

    private static ClientSSLProperties createSSLProperties() throws FileNotFoundException {
        LOGGER.info("Creating SSLProperties");
        final Resource keystore = new FileSystemResource(ResourceUtils.getFile("classpath:certificates/sysop.p12").getAbsoluteFile());
        final Resource truststore = new FileSystemResource(ResourceUtils.getFile("classpath:certificates/truststore.p12").getAbsoluteFile());
        final ClientSSLProperties retValue = new ClientSSLProperties();
        retValue.setSslEnabled(true);
        retValue.setKeyStore(keystore);
        retValue.setKeyStorePassword("123456");
        retValue.setKeyStoreType("PKCS12");
        retValue.setKeyPassword("123456");
        retValue.setTrustStore(truststore);
        retValue.setTrustStorePassword("123456");
        return retValue;
    }
}
