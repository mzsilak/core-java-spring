package eu.arrowhead.common.database.repository;

import eu.arrowhead.common.database.entity.System;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemRepository extends RefreshableRepository<System,Long> {
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public Optional<System> findBySystemNameAndAddressAndPort(final String systemName, final String address, final int port);
	public List<System> findBySystemName(final String systemName);
}