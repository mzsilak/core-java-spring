package eu.arrowhead.common.database.repository;

import eu.arrowhead.common.database.entity.Subscription;
import eu.arrowhead.common.database.entity.SubscriptionPublisherConnection;
import eu.arrowhead.common.database.entity.System;

import java.util.List;
import java.util.Set;

public interface SubscriptionPublisherConnectionRepository extends RefreshableRepository<SubscriptionPublisherConnection,Long> {
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public List<SubscriptionPublisherConnection> findAllBySystemAndAuthorized(final System providerSystem, final boolean authorized);
	public Set<SubscriptionPublisherConnection> findBySubscriptionEntry(final Subscription subscriptionEntry);
}