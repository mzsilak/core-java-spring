package eu.arrowhead.api;

public enum Protocol {

    HTTP("HTTP");

    private final String protocol;

    Protocol(final String protocol) {this.protocol = protocol;}

    public String value() {
        return protocol;
    }

    public String getInterface(final Encoding enc, final boolean secure) {
        if (secure) {
            return value() + "-SECURE-" + enc.value();
        } else {
            return value() + "-INSECURE-" + enc.value();
        }
    }
}
