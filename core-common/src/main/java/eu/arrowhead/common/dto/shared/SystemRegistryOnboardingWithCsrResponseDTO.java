package eu.arrowhead.common.dto.shared;

import eu.arrowhead.api.common.model.CertificateCreationResponseDTO;
import eu.arrowhead.api.deviceregistry.model.DeviceResponseDTO;
import eu.arrowhead.api.systemregistry.model.SystemResponseDTO;

import java.io.Serializable;
import java.util.Map;

public class SystemRegistryOnboardingWithCsrResponseDTO extends SystemRegistryOnboardingResponseDTO implements Serializable {

    //=================================================================================================
    // members
    private static final long serialVersionUID = 1L;

    public SystemRegistryOnboardingWithCsrResponseDTO() {
    }

    public SystemRegistryOnboardingWithCsrResponseDTO(final long id, final SystemResponseDTO system,
                                                      final DeviceResponseDTO provider, final String endOfValidity,
                                                      final Map<String, String> metadata, final int version, final String createdAt,
                                                      final String updatedAt,
                                                      final CertificateCreationResponseDTO certificateResponse) {
        super(id, system, provider, endOfValidity, metadata, version, createdAt, updatedAt, certificateResponse);
    }

    // this class exist to keep the structure of <operation>RequestDTO, <operation>ResponseDTO
}
