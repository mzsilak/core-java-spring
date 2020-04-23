package eu.arrowhead.api.annotations;

/**
 * Maps to HTTP Body parameter on HTTP transport
 */
public @interface PayloadParam {
    String value();
}
