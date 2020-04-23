package eu.arrowhead.core.gatekeeper.service.matchmaking;

import eu.arrowhead.api.orchestration.model.OrchestrationResultDTO;
import eu.arrowhead.api.systemregistry.model.SystemRequestDTO;
import eu.arrowhead.common.dto.internal.DTOUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Random;

public class RandomICNProviderMatchmaker implements ICNProviderMatchmakingAlgorithm {
	
	//=================================================================================================
	// members
	
	private static final Logger logger = LogManager.getLogger(RandomICNProviderMatchmaker.class);

	private Random rng;
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	/** 
	 * This algorithm returns the first (preferred) provider.
	 */
	@Override
	public OrchestrationResultDTO doMatchmaking(final List<OrchestrationResultDTO> orList, final ICNProviderMatchmakingParameters params) {
		logger.debug("RandomICNProviderMatchmaker.doMatchmaking started...");
		
		Assert.isTrue(orList != null && !orList.isEmpty(), "orList is null or empty.");
		Assert.notNull(params, "params is null");
		
		if (rng == null) {
			rng = new Random(params.getRandomSeed());
		}
		
		if (params.getPreferredLocalProviders().isEmpty()) {
			logger.debug("No preferred provider is specified, a random one in the OR list is selected.");
			return orList.get(rng.nextInt(orList.size()));
		}
		
		for (final OrchestrationResultDTO oResult : orList) {
			for (final SystemRequestDTO provider : params.getPreferredLocalProviders()) {
				if (DTOUtilities.equalsSystemInResponseAndRequest(oResult.getProvider(), provider)) {
					logger.debug("The first preferred provider found in OR is selected.");
					return oResult;
				}
			}
		}
		
		logger.debug("no match was found between preferred providers, a random one is selected.");
		
		return orList.get(rng.nextInt(orList.size()));
	}
}