package eu.arrowhead.common.database.repository;

import eu.arrowhead.common.database.entity.AuthorizationIntraCloudInterfaceConnection;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizationIntraCloudInterfaceConnectionRepository extends RefreshableRepository<AuthorizationIntraCloudInterfaceConnection,Long> {
}