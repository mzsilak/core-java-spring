package eu.arrowhead.common.api;

/**
 * An attempt to have protocol independent methods
 */
public enum ApiMethod {
    /**
     * Same as HTTP POST
     */
    CREATE,
    /**
     * Same as HTTP GET
     */
    READ,
    /**
     * Same as HTTP PUT
     */
    UPDATE,
    /**
     * Same as HTTP PATCH
     */
    MERGE,
    /**
     * Same as HTTP DELETE
     */
    DELETE
}
