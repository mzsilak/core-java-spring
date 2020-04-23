package eu.arrowhead.common.transport;

import eu.arrowhead.common.api.Protocol;

public class UnsupportedProtocolException extends RuntimeException {
    public UnsupportedProtocolException() { super(); }

    public UnsupportedProtocolException(final String msg) {
        super(msg);
    }

    public UnsupportedProtocolException(final String clsName, final Protocol protocol) {
        super(clsName + " does not support " + protocol);
    }
}
