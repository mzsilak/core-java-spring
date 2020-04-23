package eu.arrowhead.common.dto.internal;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class CertificateSigningRequestDTO implements Serializable {

    private static final long serialVersionUID = -6810780579000655432L;

    @NotBlank(message = "The encodedCSR is mandatory")
    private String encodedCSR;

    public CertificateSigningRequestDTO() {}

    public CertificateSigningRequestDTO(String encodedCSR) {
        this.encodedCSR = encodedCSR;
    }

    public String getEncodedCSR() {
        return encodedCSR;
    }

    public void setEncodedCSR(String encodedCSR) {
        this.encodedCSR = encodedCSR;
    }
}
