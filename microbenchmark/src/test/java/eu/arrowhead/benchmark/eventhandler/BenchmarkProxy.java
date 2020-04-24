package eu.arrowhead.benchmark.eventhandler;

import eu.arrowhead.api.eventhandler.EventHandlerApi;
import eu.arrowhead.api.transport.HttpInvocationHandler;
import eu.arrowhead.api.transport.TransportCache;
import eu.arrowhead.common.ClientSSLProperties;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.util.UriComponents;

import java.io.FileNotFoundException;
import java.lang.reflect.Proxy;
import java.util.Objects;

import static eu.arrowhead.common.CommonConstants.CORE_SERVICE_EVENT_HANDLER_PUBLISH;

@State(Scope.Benchmark)
public class BenchmarkProxy extends AbstractEventHandlerBenchmark {

    private HttpInvocationHandler invocationHandler;
    private EventHandlerApi eventHandlerApi;

    public BenchmarkProxy() { super(); }

    public static void main(String[] args) throws Exception {
        BenchmarkProxy proxy = new BenchmarkProxy();
        try {
            proxy.initBenchmark();
            proxy.performBenchmark();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            proxy.cleanBenchmark();
        }
    }

    @Override
    @Setup(Level.Trial)
    public void initBenchmark() throws Exception {
        super.initBenchmark();
        invocationHandler = createInvocationHandler();
        eventHandlerApi = (EventHandlerApi) Proxy.newProxyInstance(EventHandlerApi.class.getClassLoader(),
                                                                   new Class[]{EventHandlerApi.class},
                                                                   invocationHandler);
    }

    @Override
    @TearDown
    public void cleanBenchmark() throws Exception {
        if (Objects.nonNull(invocationHandler)) { invocationHandler.stop(); }
        super.cleanBenchmark();
    }

    @Override
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void performBenchmark() {
        eventHandlerApi.publish(getPayload());
    }

    private HttpInvocationHandler createInvocationHandler() throws Exception {
        return new HttpInvocationHandler(system(),
                                         createSrQueryUri(),
                                         createTransportCache(),
                                         createSSLProperties());
    }

    private TransportCache<UriComponents> createTransportCache() {
        final TransportCache<UriComponents> cache = new TransportCache<>();
        cache.put(CORE_SERVICE_EVENT_HANDLER_PUBLISH, createPublishEventUri());
        return cache;
    }

    private ClientSSLProperties createSSLProperties() throws FileNotFoundException {
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
