package eu.arrowhead.api.transport;

import org.springframework.http.HttpHeaders;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class HttpHeadersConsumer implements Consumer<HttpHeaders> {
    private final Map<String, String> headers;

    public HttpHeadersConsumer() {
        this(Map.of());
    }

    public HttpHeadersConsumer(final Map<String, String> headers) {
        this.headers = Objects.requireNonNull(headers);
    }

    @Override
    public void accept(final HttpHeaders httpHeaders) {
        headers.forEach(httpHeaders::add);
    }
}
