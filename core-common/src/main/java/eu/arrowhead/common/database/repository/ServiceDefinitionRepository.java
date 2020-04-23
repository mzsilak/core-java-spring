package eu.arrowhead.common.database.repository;

import eu.arrowhead.common.database.entity.ServiceDefinition;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceDefinitionRepository extends RefreshableRepository<ServiceDefinition,Long> {
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public Optional<ServiceDefinition> findByServiceDefinition(final String serviceDefinition);	
}