package eu.arrowhead.benchmark.eventhandler;

import eu.arrowhead.common.SSLProperties;
import eu.arrowhead.common.drivers.DriverUtilities;
import eu.arrowhead.common.drivers.EventDriver;
import eu.arrowhead.common.http.HttpService;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;

import java.util.HashMap;

public class BenchmarkDriver extends AbstractEventHandlerBenchmark {

    private EventDriver driver;

    public BenchmarkDriver() { super(); }

    public static void main(String[] args) throws Exception {
        BenchmarkDriver proxy = new BenchmarkDriver();
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
        final HttpService httpService = createHttpService();
        final SSLProperties sslProperties = createSslProperties();
        final DriverUtilities driverUtilities = new DriverUtilities(createSrQueryUri(), system(), httpService, sslProperties, new HashMap<>());

        driver = new EventDriver(driverUtilities, httpService);
    }

    @Override
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void performBenchmark() {
        driver.publish(getPayload());
    }

    @Override
    @TearDown
    public void cleanBenchmark() throws Exception {
        super.cleanBenchmark();
    }
}
