package eu.arrowhead.core.gams.database.repositories;

import eu.arrowhead.common.database.repository.RefreshableRepository;
import eu.arrowhead.core.gams.database.entities.GamsInstance;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GamsInstanceRepository extends RefreshableRepository<GamsInstance, Long> {

    <S extends GamsInstance> Optional<S> findByUid(final UUID uid);

    <S extends GamsInstance> Optional<S> findByName(final String name);
}