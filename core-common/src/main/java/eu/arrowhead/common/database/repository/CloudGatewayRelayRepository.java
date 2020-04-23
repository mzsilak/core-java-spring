package eu.arrowhead.common.database.repository;

import eu.arrowhead.common.database.entity.CloudGatewayRelay;
import org.springframework.stereotype.Repository;

@Repository
public interface CloudGatewayRelayRepository extends RefreshableRepository<CloudGatewayRelay,Long> {
}