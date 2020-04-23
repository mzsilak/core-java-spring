package eu.arrowhead.api.common.model;

import java.io.Serializable;
import java.util.StringJoiner;

public class CertificateCreationResponseDTO implements Serializable {

    //=================================================================================================
    // members
    private static final long serialVersionUID = 1L;

    private String certificate;
    private CertificateType certificateType;
    private KeyPairDTO keyPairDTO;
    private String certificateFormat;

    //=================================================================================================
    // constructors

    public CertificateCreationResponseDTO() {
    }

    public CertificateCreationResponseDTO(final String certificate, final CertificateType certificateType, final String certificateFormat) {
        this.certificate = certificate;
        this.certificateType = certificateType;
        this.certificateFormat = certificateFormat;
    }

    public CertificateCreationResponseDTO(final String certificate,
                                          final CertificateType certificateType,
                                          final KeyPairDTO keyPairDTO,
                                          final String certificateFormat) {
        this.certificate = certificate;
        this.certificateType = certificateType;
        this.keyPairDTO = keyPairDTO;
        this.certificateFormat = certificateFormat;
    }

    //=================================================================================================
    // methods

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getCertificateFormat() {
        return certificateFormat;
    }

    public void setCertificateFormat(final String certificateFormat) {
        this.certificateFormat = certificateFormat;
    }

    public CertificateType getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(final CertificateType certificateType) {
        this.certificateType = certificateType;
    }

    public KeyPairDTO getKeyPairDTO() {
        return keyPairDTO;
    }

    public void setKeyPairDTO(final KeyPairDTO keyPairDTO) {
        this.keyPairDTO = keyPairDTO;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CertificateCreationResponseDTO.class.getSimpleName() + "[", "]")
                .add("certificate='" + certificate + "'")
                .add("certificateFormat=" + certificateFormat)
                .add("certificateType=" + certificateType)
                .add("keyPairDTO=" + keyPairDTO)
                .toString();
    }
}
