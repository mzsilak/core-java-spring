package eu.arrowhead.benchmark.eventhandler;

import eu.arrowhead.api.systemregistry.model.SystemRequestDTO;
import eu.arrowhead.api.systemregistry.model.SystemResponseDTO;
import eu.arrowhead.common.SSLProperties;
import eu.arrowhead.common.dto.internal.SystemListResponseDTO;
import eu.arrowhead.common.http.HttpService;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.util.ResourceUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.FileNotFoundException;
import java.util.Objects;

public abstract class AbstractBenchmark {

    public void initBenchmark() throws Exception {
        final HttpService httpService = createHttpService();
        try {
            httpService.sendRequest(createSystemUri(), HttpMethod.POST, SystemResponseDTO.class, system());
        } catch(final Exception ex) {
            cleanBenchmark();
            throw ex;
        }
    }

    public abstract void performBenchmark();

    public void cleanBenchmark() throws Exception {
        final HttpService httpService = createHttpService();
        final var listEntity = httpService.sendRequest(findAllSystemsUri(), HttpMethod.GET, SystemListResponseDTO.class);
        final var system = system();

        final SystemListResponseDTO body = Objects.requireNonNull(listEntity.getBody());
        for (SystemResponseDTO dto : body.getData()) {
            if (system.getSystemName().equalsIgnoreCase(dto.getSystemName())) {
                final UriComponents uriComponents = deleteSystemUri().expand(dto.getId());
                httpService.sendRequest(uriComponents, HttpMethod.DELETE, Void.class);
            }
        }
    }

    protected HttpService createHttpService() throws Exception {
        final HttpService httpService = new HttpService(createSslProperties());
        httpService.init();
        return httpService;
    }

    protected SSLProperties createSslProperties() throws FileNotFoundException {
        final String password = "123456";
        final Resource keystore = new FileSystemResource(ResourceUtils.getFile("classpath:certificates/sysop.p12").getAbsoluteFile());
        final Resource truststore = new FileSystemResource(ResourceUtils.getFile("classpath:certificates/truststore.p12").getAbsoluteFile());
        return new SSLProperties(true, "PKCS12", keystore, password, password, truststore, password);
    }

    protected SystemRequestDTO system() {
        return new SystemRequestDTO("sysop", "127.0.0.1", 1234, null);
    }

    protected UriComponents createSrQueryUri() {
        return UriComponentsBuilder.fromUriString("https://127.0.0.1:8443/serviceregistry/query").build();
    }

    protected UriComponents createSystemUri() {
        return UriComponentsBuilder.fromUriString("https://127.0.0.1:8443/serviceregistry/mgmt/systems").build();
    }

    protected UriComponents deleteSystemUri() {
        return UriComponentsBuilder.fromUriString("https://127.0.0.1:8443/serviceregistry/mgmt/systems/{id}").build();
    }

    protected UriComponents findAllSystemsUri() {
        return UriComponentsBuilder.fromUriString("https://127.0.0.1:8443/serviceregistry/mgmt/systems/").build();
    }
}
