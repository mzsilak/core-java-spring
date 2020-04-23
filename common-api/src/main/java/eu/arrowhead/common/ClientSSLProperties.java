package eu.arrowhead.common;

import org.springframework.core.io.Resource;

public class ClientSSLProperties {

    //=================================================================================================
    // members
    private boolean sslEnabled;
    private String keyStoreType;
    private Resource keyStore;
    private String keyStorePassword;
    private String keyPassword;
    private Resource trustStore;
    private String trustStorePassword;

    //=================================================================================================
    // methods

    //-------------------------------------------------------------------------------------------------

    public boolean isSslEnabled() {
        return sslEnabled;
    }

    public void setSslEnabled(final boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    public String getKeyStoreType() {
        return keyStoreType;
    }

    public void setKeyStoreType(final String keyStoreType) {
        this.keyStoreType = keyStoreType;
    }

    public Resource getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(final Resource keyStore) {
        this.keyStore = keyStore;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(final String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getKeyPassword() {
        return keyPassword;
    }

    public void setKeyPassword(final String keyPassword) {
        this.keyPassword = keyPassword;
    }

    public Resource getTrustStore() {
        return trustStore;
    }

    public void setTrustStore(final Resource trustStore) {
        this.trustStore = trustStore;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public void setTrustStorePassword(final String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }
}