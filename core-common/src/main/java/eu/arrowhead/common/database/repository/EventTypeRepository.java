package eu.arrowhead.common.database.repository;

import eu.arrowhead.common.database.entity.EventType;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventTypeRepository extends RefreshableRepository<EventType,Long> {

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public Optional<EventType> findByEventTypeName(final String eventTypeName);
}