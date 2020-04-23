package eu.arrowhead.common.database.repository;

import eu.arrowhead.common.database.entity.ServiceRegistryInterfaceConnection;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRegistryInterfaceConnectionRepository extends RefreshableRepository<ServiceRegistryInterfaceConnection,Long> {
}