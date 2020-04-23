package eu.arrowhead.common.api.annotations;

/**
 * A meta parameter which is not directly related to the api operation. Maps to HTTP Header on HTTP transport
 */
public @interface MetaParam {
    String value();
}
