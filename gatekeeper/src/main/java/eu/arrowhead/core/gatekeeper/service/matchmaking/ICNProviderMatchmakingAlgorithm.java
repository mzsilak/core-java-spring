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

import eu.arrowhead.common.dto.shared.OrchestrationResultDTO;

public interface ICNProviderMatchmakingAlgorithm {

	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public OrchestrationResultDTO doMatchmaking(final List<OrchestrationResultDTO> orList, final ICNProviderMatchmakingParameters params);
}