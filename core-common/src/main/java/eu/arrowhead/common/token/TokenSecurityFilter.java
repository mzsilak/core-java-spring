package eu.arrowhead.common.token;

import eu.arrowhead.api.common.exception.ArrowheadException;
import eu.arrowhead.api.common.exception.AuthException;
import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.SecurityUtilities;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.filter.ArrowheadFilter;
import eu.arrowhead.common.token.TokenUtilities.TokenInfo;
import org.springframework.lang.Nullable;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;

public abstract class TokenSecurityFilter extends ArrowheadFilter {

	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			log.debug("Checking access in TokenSecurityFilter...");
			try {
				final HttpServletRequest httpRequest = (HttpServletRequest) request;
				final String requestTarget = Utilities.stripEndSlash(httpRequest.getRequestURL().toString());
				final String clientCN = getCertificateCNFromRequest(httpRequest);
				if (clientCN == null) {
					log.error("Unauthorized access: {}", requestTarget);
					throw new AuthException("Unauthorized access: " + requestTarget);
				}
				
				final String token = httpRequest.getParameter(CommonConstants.REQUEST_PARAM_TOKEN);
				if (Utilities.isEmpty(token)) {
					log.error("Unauthorized access: {}, no token is specified", requestTarget);
					throw new AuthException("Unauthorized access: " + requestTarget + ", no token is specified");
				}
				
				checkToken(clientCN, token, requestTarget);
			} catch (final ArrowheadException ex) {
				handleException(ex, response);
			}
		}
		
		chain.doFilter(request, response);
	}
	
	//=================================================================================================
	// assistant methods
	
	//-------------------------------------------------------------------------------------------------
	protected abstract PrivateKey getMyPrivateKey();
	
	//-------------------------------------------------------------------------------------------------
	protected abstract PublicKey getAuthorizationPublicKey();
	
	//-------------------------------------------------------------------------------------------------
	protected TokenSecurityFilter() {}
	
	//-------------------------------------------------------------------------------------------------
	protected TokenInfo checkToken(final String clientCN, final String token, final String requestTarget) {
		final String clientName = clientCN.split("\\.")[0];
		final TokenInfo tokenInfo = TokenUtilities.validateTokenAndExtractTokenInfo(token, getAuthorizationPublicKey(), getMyPrivateKey()); // expiration (if set) is already checked in this method
		if (!clientName.equalsIgnoreCase(tokenInfo.getConsumerName())) {
			log.error("Client CN ({}) and token information ({}) is mismatched at: {}", clientCN, tokenInfo.getConsumerName(), requestTarget);
			throw new AuthException("Unauthorized accesss: " + requestTarget + ", invalid token.");
		}
		
		return tokenInfo;
	}

	//-------------------------------------------------------------------------------------------------
	@Nullable
	private String getCertificateCNFromRequest(final HttpServletRequest request) {
		return SecurityUtilities.getCertificateCNFromRequest(request);
	}
}