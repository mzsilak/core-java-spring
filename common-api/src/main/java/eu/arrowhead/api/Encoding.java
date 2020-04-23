package eu.arrowhead.api;

public enum Encoding {

    JSON("JSON");

    private final String type;

    Encoding(final String type) {
        this.type = type;
    }

    public String value() {
        return type;
    }

    public String getInterface(final Protocol protocol, final boolean secure) {
        if (secure) {
            return protocol.value() + "-SECURE-" + value();
        } else {
            return protocol.value() + "-INSECURE-" + value();
        }
    }

}
