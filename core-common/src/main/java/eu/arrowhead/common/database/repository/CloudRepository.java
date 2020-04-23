package eu.arrowhead.common.database.repository;

import eu.arrowhead.common.database.entity.Cloud;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CloudRepository extends RefreshableRepository<Cloud,Long> {

	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public List<Cloud> findByOwnCloudAndSecure(final boolean ownCloud, final boolean secure);
	public Optional<Cloud> findByOperatorAndName(final String operator, final String name);
	public List<Cloud> findByNeighbor(final boolean neighbor);
	public boolean existsByOperatorAndName(final String operator, final String name); 
}