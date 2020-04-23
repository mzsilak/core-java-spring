package eu.arrowhead.common.api.annotations;

/**
 * Maps to HTTP Body parameter on HTTP transport
 */
public @interface PayloadParam {
    String value();
}
