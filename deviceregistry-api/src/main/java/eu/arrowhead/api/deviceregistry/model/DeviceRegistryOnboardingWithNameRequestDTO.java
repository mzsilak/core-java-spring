package eu.arrowhead.api.deviceregistry.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import eu.arrowhead.api.common.model.CertificateCreationRequestDTO;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

@JsonInclude(Include.NON_NULL)
public class DeviceRegistryOnboardingWithNameRequestDTO extends DeviceRegistryRequestDTO implements Serializable {

    //=================================================================================================
    // members

    private static final long serialVersionUID = -635438605292398404L;
    private CertificateCreationRequestDTO certificateCreationRequest;

    //=================================================================================================
    // methods

    public DeviceRegistryOnboardingWithNameRequestDTO() {
    }

    //-------------------------------------------------------------------------------------------------
    public DeviceRegistryOnboardingWithNameRequestDTO(final DeviceRequestDTO device, final String endOfValidity) {
        super(device, endOfValidity);
    }

    //-------------------------------------------------------------------------------------------------
    public DeviceRegistryOnboardingWithNameRequestDTO(final String deviceName, final String macAddress, final String endOfValidity) {
        super(new DeviceRequestDTO(deviceName, macAddress), endOfValidity);
        this.certificateCreationRequest = new CertificateCreationRequestDTO(deviceName);
    }

    //-------------------------------------------------------------------------------------------------
    public DeviceRegistryOnboardingWithNameRequestDTO(final DeviceRequestDTO device,
                                                      final String endOfValidity,
                                                      final Map<String, String> metadata,
                                                      final Integer version) {
        super(device, endOfValidity, metadata, version);
        this.certificateCreationRequest = new CertificateCreationRequestDTO(Objects.requireNonNull(device).getDeviceName());
    }

    //-------------------------------------------------------------------------------------------------
    public CertificateCreationRequestDTO getCertificateCreationRequest() {
        return certificateCreationRequest;
    }

    public void setCertificateCreationRequest(final CertificateCreationRequestDTO certificateCreationRequest) {
        this.certificateCreationRequest = certificateCreationRequest;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DeviceRegistryOnboardingWithNameRequestDTO.class.getSimpleName() + "[", "]")
                .add("certificateCreationRequest=" + certificateCreationRequest)
                .add("parent=" + super.toString())
                .toString();
    }
}