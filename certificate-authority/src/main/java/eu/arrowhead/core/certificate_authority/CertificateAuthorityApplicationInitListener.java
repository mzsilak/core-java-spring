package eu.arrowhead.core.certificate_authority;

import eu.arrowhead.common.ApplicationInitListener;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.security.Security;

@Component
public class CertificateAuthorityApplicationInitListener extends ApplicationInitListener {

    @Override
    protected void customInit(ContextRefreshedEvent event) {
        logger.debug("customInit started...");

        Security.addProvider(new BouncyCastleProvider());
    }

}
