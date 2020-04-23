package eu.arrowhead.common.database.repository;

import eu.arrowhead.common.database.entity.AuthorizationInterCloud;
import eu.arrowhead.common.database.entity.Cloud;
import eu.arrowhead.common.database.entity.ServiceDefinition;
import eu.arrowhead.common.database.entity.System;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorizationInterCloudRepository extends RefreshableRepository<AuthorizationInterCloud,Long> {
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public Optional<AuthorizationInterCloud> findByCloudAndServiceDefinition(final Cloud cloud, final ServiceDefinition serviceDefinition);
	public Optional<AuthorizationInterCloud> findByCloudAndProviderAndServiceDefinition(final Cloud cloud, final System provider, final ServiceDefinition serviceDefinition);
	public Optional<AuthorizationInterCloud> findByCloudIdAndProviderIdAndServiceDefinitionId(final long cloudId, final long providerId, final long serviceDefinitionId);
}