package eu.arrowhead.benchmark.eventhandler;

import eu.arrowhead.api.eventhandler.model.EventPublishRequestDTO;
import eu.arrowhead.common.Utilities;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.ZonedDateTime;
import java.util.Map;

@State(Scope.Benchmark)
public abstract class AbstractEventHandlerBenchmark extends AbstractBenchmark {
    public AbstractEventHandlerBenchmark() { super(); }

    private EventPublishRequestDTO dto;

    @Override
    public void initBenchmark() throws Exception {
        super.initBenchmark();
        dto = new EventPublishRequestDTO("TestEvent", system(),
                                         Map.of(), "payload",
                                         Utilities.convertZonedDateTimeToUTCString(ZonedDateTime.now()));
    }

    protected EventPublishRequestDTO getPayload() {
        dto.setTimeStamp(Utilities.convertZonedDateTimeToUTCString(ZonedDateTime.now()));
        return dto;
    }

    protected UriComponents createPublishEventUri() {
        return UriComponentsBuilder.fromUriString("https://127.0.0.1:8455/eventhandler/publish").build();
    }

    protected UriComponents createEchoEventUri() {
        return UriComponentsBuilder.fromUriString("https://127.0.0.1:8455/eventhandler/echo").build();
    }
}
