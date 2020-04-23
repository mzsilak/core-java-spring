package eu.arrowhead.api.transport;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TransportCache<P> {

    private final ConcurrentMap<String, P> cache = new ConcurrentHashMap<>();

    public TransportCache() { super(); }

    public Optional<P> get(final String key) {
        return Optional.ofNullable(cache.get(key));
    }

    public void put(final String key, final P object) {
        cache.put(key, object);
    }

    public void remove(final String key) {
        cache.remove(key);
    }
}
