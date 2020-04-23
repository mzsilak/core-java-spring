package eu.arrowhead.deviceregistry.api;

import eu.arrowhead.common.transport.HttpInvocationHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Proxy;

public class Main {

    public static void main(String[] args) {
        final DeviceRegistryApi instance = (DeviceRegistryApi) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{DeviceRegistryApi.class},
                                                                                      new HttpInvocationHandler(
                                                                                              UriComponentsBuilder.fromUriString(
                                                                                                      "https://127.0.0.1:8437/serviceregistry/query").build()));

        instance.registerDevice(null);
    }
}
