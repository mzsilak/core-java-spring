package eu.arrowhead.common.database.repository;

import eu.arrowhead.common.database.entity.ChoreographerPlan;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChoreographerPlanRepository extends RefreshableRepository<ChoreographerPlan,Long> {
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
    public Optional<ChoreographerPlan> findByName(final String name);
}