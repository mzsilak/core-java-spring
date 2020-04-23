package eu.arrowhead.common.database.repository;

import eu.arrowhead.common.database.entity.ChoreographerAction;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChoreographerActionRepository extends RefreshableRepository<ChoreographerAction,Long> {

    //=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public Optional<ChoreographerAction> findByName(final String name);
	public Optional<ChoreographerAction> findByNameAndPlanId(final String name, final long planId);
    //public Optional<ChoreographerAction> findByActionNameAndNextAction(final String actionName, final ChoreographerAction nextAction);
}