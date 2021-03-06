/********************************************************************************
 * Copyright (c) 2019 AITIA
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   AITIA - implementation
 *   Arrowhead Consortia - conceptualization
 ********************************************************************************/

package eu.arrowhead.core.gatekeeper.service.matchmaking;

import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import eu.arrowhead.common.dto.internal.DTOUtilities;
import eu.arrowhead.common.dto.shared.OrchestrationResultDTO;
import eu.arrowhead.common.dto.shared.SystemRequestDTO;

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
			if (params.isOnlyPreferred()) {
				return null;
			}
			
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