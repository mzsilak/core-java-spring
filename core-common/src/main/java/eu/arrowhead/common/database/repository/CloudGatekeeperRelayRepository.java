package eu.arrowhead.common.database.repository;

import eu.arrowhead.common.database.entity.CloudGatekeeperRelay;
import org.springframework.stereotype.Repository;

@Repository
public interface CloudGatekeeperRelayRepository extends RefreshableRepository<CloudGatekeeperRelay,Long> {
}