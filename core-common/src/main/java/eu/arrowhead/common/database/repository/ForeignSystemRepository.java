package eu.arrowhead.common.database.repository;

import eu.arrowhead.common.database.entity.Cloud;
import eu.arrowhead.common.database.entity.ForeignSystem;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForeignSystemRepository extends RefreshableRepository<ForeignSystem,Long> {
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public Optional<ForeignSystem> findBySystemNameAndAddressAndPortAndProviderCloud(final String systemName, final String address, final int port, final Cloud providerCloud);
}