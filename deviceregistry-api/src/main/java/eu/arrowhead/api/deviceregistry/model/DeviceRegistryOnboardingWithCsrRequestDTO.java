package eu.arrowhead.api.deviceregistry.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.util.Map;
import java.util.StringJoiner;

@JsonInclude(Include.NON_NULL)
public class DeviceRegistryOnboardingWithCsrRequestDTO extends DeviceRegistryRequestDTO implements Serializable {

    //=================================================================================================
    // members

    private static final long serialVersionUID = -635438605292398404L;
    private String certificateSigningRequest;

    //=================================================================================================
    // methods

    public DeviceRegistryOnboardingWithCsrRequestDTO() {
    }

    //-------------------------------------------------------------------------------------------------
    public DeviceRegistryOnboardingWithCsrRequestDTO(final DeviceRequestDTO device, final String endOfValidity, final String certificateSigningRequest) {
        super(device, endOfValidity);
        this.certificateSigningRequest = certificateSigningRequest;
    }

    //-------------------------------------------------------------------------------------------------
    public DeviceRegistryOnboardingWithCsrRequestDTO(final String deviceName, final String macAddress, final String endOfValidity, final String certificateSigningRequest) {
        super(new DeviceRequestDTO(deviceName, macAddress), endOfValidity);
        this.certificateSigningRequest = certificateSigningRequest;
    }

    //-------------------------------------------------------------------------------------------------
    public DeviceRegistryOnboardingWithCsrRequestDTO(final DeviceRequestDTO device,
                                                     final String endOfValidity,
                                                     final Map<String, String> metadata,
                                                     final Integer version, final String certificateSigningRequest) {
        super(device, endOfValidity, metadata, version);
        this.certificateSigningRequest = certificateSigningRequest;
    }

    //-------------------------------------------------------------------------------------------------
    public String getCertificateSigningRequest() {
        return certificateSigningRequest;
    }

    public void setCertificateSigningRequest(final String certificateSigningRequest) {
        this.certificateSigningRequest = certificateSigningRequest;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DeviceRegistryOnboardingWithCsrRequestDTO.class.getSimpleName() + "[", "]")
                .add("certificateSigningRequest=" + certificateSigningRequest)
                .add("parent=" + super.toString())
                .toString();
    }
}