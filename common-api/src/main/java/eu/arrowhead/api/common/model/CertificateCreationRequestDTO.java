package eu.arrowhead.api.common.model;

import java.io.Serializable;
import java.util.StringJoiner;

public class CertificateCreationRequestDTO implements Serializable {

    //=================================================================================================
    // members
    private static final long serialVersionUID = 1L;

    private String commonName;
    private KeyPairDTO keyPairDTO;


    //=================================================================================================
    // constructors

    public CertificateCreationRequestDTO() {
    }

    public CertificateCreationRequestDTO(final String commonName) {
        this.commonName = commonName;
    }

    public CertificateCreationRequestDTO(final String commonName, final KeyPairDTO keyPairDTO) {
        this.commonName = commonName;
        this.keyPairDTO = keyPairDTO;
    }

    //=================================================================================================
    // methods

    public String getCommonName() {
        return commonName;
    }
    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public KeyPairDTO getKeyPairDTO() {
        return keyPairDTO;
    }

    public void setKeyPairDTO(final KeyPairDTO keyPairDTO) {
        this.keyPairDTO = keyPairDTO;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CertificateCreationRequestDTO.class.getSimpleName() + "[", "]")
                .add("commonName='" + commonName + "'")
                .add("keyPairDTO=" + keyPairDTO)
                .toString();
    }
}
