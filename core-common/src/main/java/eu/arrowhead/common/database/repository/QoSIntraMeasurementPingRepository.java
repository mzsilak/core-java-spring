package eu.arrowhead.common.database.repository;

import eu.arrowhead.common.database.entity.QoSIntraMeasurement;
import eu.arrowhead.common.database.entity.QoSIntraPingMeasurement;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QoSIntraMeasurementPingRepository extends RefreshableRepository<QoSIntraPingMeasurement,Long> {

	//=================================================================================================
	public Optional<QoSIntraPingMeasurement> findByMeasurement(final QoSIntraMeasurement measurement);
}