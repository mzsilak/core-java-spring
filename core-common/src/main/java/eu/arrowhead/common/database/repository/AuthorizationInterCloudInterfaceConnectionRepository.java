package eu.arrowhead.common.database.repository;

import eu.arrowhead.common.database.entity.AuthorizationInterCloudInterfaceConnection;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizationInterCloudInterfaceConnectionRepository extends RefreshableRepository<AuthorizationInterCloudInterfaceConnection,Long> {
}