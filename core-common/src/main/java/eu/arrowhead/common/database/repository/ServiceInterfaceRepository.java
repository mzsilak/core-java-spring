package eu.arrowhead.common.database.repository;

import eu.arrowhead.common.database.entity.ServiceInterface;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceInterfaceRepository extends RefreshableRepository<ServiceInterface,Long> {

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public Optional<ServiceInterface> findByInterfaceName(final String interfaceName);
}