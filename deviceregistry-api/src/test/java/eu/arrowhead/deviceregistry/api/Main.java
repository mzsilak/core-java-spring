package eu.arrowhead.deviceregistry.api;

import eu.arrowhead.common.ClientSSLProperties;
import eu.arrowhead.common.transport.HttpInvocationHandler;
import eu.arrowhead.common.transport.TransportCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.FileNotFoundException;
import java.lang.reflect.Proxy;

import static eu.arrowhead.common.CommonConstants.CORE_SERVICE_DEVICE_REGISTRY_REGISTER;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) throws Exception {

        final ClientSSLProperties sslProperties = createSSLProperties();
        final TransportCache<UriComponents> cache = new TransportCache<>();
        final UriComponents srUri = UriComponentsBuilder.fromUriString("https://127.0.0.1:8443/serviceregistry/query").build();

        cache.put(CORE_SERVICE_DEVICE_REGISTRY_REGISTER, UriComponentsBuilder.fromUriString("https://127.0.0.1:8439/deviceregistry/register").build());
        cache.put("service-query", srUri);

        final HttpInvocationHandler invocationHandler = new HttpInvocationHandler(srUri, cache, sslProperties);
        final DeviceRegistryApi instance = (DeviceRegistryApi) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                                                                                      new Class[]{DeviceRegistryApi.class},
                                                                                      invocationHandler);

        Thread.sleep(500L);
        instance.registerDevice(null);
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
