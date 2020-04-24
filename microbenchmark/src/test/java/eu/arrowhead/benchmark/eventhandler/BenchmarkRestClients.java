package eu.arrowhead.benchmark.eventhandler;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.TimeUnit;

public class BenchmarkRestClients {


    public static void main(String[] args) throws Exception {

        Options options = new OptionsBuilder()
                .include(BenchmarkHttpService.class.getSimpleName())
                .include(BenchmarkDriver.class.getSimpleName())
                .include(BenchmarkProxy.class.getSimpleName())
                .threads(20).forks(1).shouldFailOnError(true).shouldDoGC(true)
                .warmupIterations(10)
                .measurementIterations(5)
                .measurementTime(TimeValue.seconds(30))
                .jvmArgs("-server").build();
        new Runner(options).run();
    }
}
