package eu.arrowhead.common.database.repository;

import eu.arrowhead.common.database.entity.QoSIntraPingMeasurementLog;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Optional;

@Repository
public interface QoSIntraPingMeasurementLogRepository extends RefreshableRepository<QoSIntraPingMeasurementLog, Long> {

	//=================================================================================================
	// methods
	public Optional<QoSIntraPingMeasurementLog> findByMeasuredAt(final ZonedDateTime timeStamp);
}
