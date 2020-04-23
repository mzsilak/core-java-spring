package eu.arrowhead.deviceregistry.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import eu.arrowhead.common.api.model.CertificateCreationResponseDTO;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Map;
import java.util.StringJoiner;

@JsonInclude(Include.NON_NULL)
public abstract class DeviceRegistryOnboardingResponseDTO extends DeviceRegistryResponseDTO implements Serializable {

    //=================================================================================================
    // members

    private static final long serialVersionUID = -635438605292398404L;
    private CertificateCreationResponseDTO certificateResponse;

    //=================================================================================================
    // methods

    public DeviceRegistryOnboardingResponseDTO() {
    }

    public DeviceRegistryOnboardingResponseDTO(final long id, final DeviceResponseDTO device, final String endOfValidity,
                                               final Map<String, String> metadata, final int version, final String createdAt, final String updatedAt,
                                               final CertificateCreationResponseDTO certificateResponse) {
        super(id, device, endOfValidity, metadata, version, createdAt, updatedAt);
        this.certificateResponse = certificateResponse;
    }

    public void load(final DeviceRegistryResponseDTO dto) {
        Assert.notNull(dto, "DeviceRegistryResponseDTO must not be null");

        setId(dto.getId());
        setDevice(dto.getDevice());
        setEndOfValidity(dto.getEndOfValidity());
        setMetadata(dto.getMetadata());
        setVersion(dto.getVersion());
        setCreatedAt(dto.getCreatedAt());
        setUpdatedAt(dto.getUpdatedAt());
    }

    //-------------------------------------------------------------------------------------------------
    public CertificateCreationResponseDTO getCertificateResponse() {
        return certificateResponse;
    }

    public void setCertificateResponse(final CertificateCreationResponseDTO certificateResponse) {
        this.certificateResponse = certificateResponse;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DeviceRegistryOnboardingResponseDTO.class.getSimpleName() + "[", "]")
                .add("certificateResponse=" + certificateResponse)
                .add("parent=" + super.toString())
                .toString();
    }
}