package eu.arrowhead.common.database.repository;

import eu.arrowhead.common.database.entity.ChoreographerStep;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChoreographerStepRepository extends RefreshableRepository<ChoreographerStep,Long> {
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
    public Optional<ChoreographerStep> findByName(final String name);
    public Optional<ChoreographerStep> findByNameAndActionId(final String name, final long actionId);
}