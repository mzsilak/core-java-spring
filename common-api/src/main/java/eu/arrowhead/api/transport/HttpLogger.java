package eu.arrowhead.api.transport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

import java.util.List;

public class HttpLogger {

    private final Logger logger;

    public HttpLogger(final Logger logger) {
        this.logger = logger;
    }

    public ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> {
            logger.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
            logger.trace("--- Http Headers: ---");
            clientRequest.headers().forEach(this::logHeader);
            logger.trace("--- Http Cookies: ---");
            clientRequest.cookies().forEach(this::logHeader);
            return next.exchange(clientRequest);
        };
    }

    public ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            logger.debug("Response: {}", clientResponse.statusCode());
            clientResponse.headers().asHttpHeaders()
                          .forEach((name, values) -> values.forEach(value -> logger.trace("{}={}", name, value)));
            return Mono.just(clientResponse);
        });
    }

    private void logHeader(String name, List<String> values) {
        values.forEach(value -> logger.trace("{}={}", name, value));
    }
}
