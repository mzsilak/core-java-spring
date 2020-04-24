package eu.arrowhead.benchmark.eventhandler;

import eu.arrowhead.common.http.HttpService;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.springframework.http.HttpMethod;

@State(Scope.Benchmark)
public class BenchmarkHttpService extends AbstractEventHandlerBenchmark {

    private HttpService httpService;


    public BenchmarkHttpService() { super(); }

    public static void main(String[] args) throws Exception {
        BenchmarkHttpService proxy = new BenchmarkHttpService();
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
        httpService = createHttpService();
    }

    @Override
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void performBenchmark() {
        httpService.sendRequest(createEchoEventUri(), HttpMethod.GET, String.class);
        httpService.sendRequest(createPublishEventUri(), HttpMethod.POST, Void.class, getPayload());
    }

    @Override
    @TearDown
    public void cleanBenchmark() throws Exception {
        super.cleanBenchmark();
    }
}
