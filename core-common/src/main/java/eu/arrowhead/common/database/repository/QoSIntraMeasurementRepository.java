package eu.arrowhead.common.database.repository;

import eu.arrowhead.common.database.entity.QoSIntraMeasurement;
import eu.arrowhead.common.database.entity.System;
import eu.arrowhead.common.dto.shared.QoSMeasurementType;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QoSIntraMeasurementRepository extends RefreshableRepository<QoSIntraMeasurement,Long> {

	//=================================================================================================
	// methods
	public Optional<QoSIntraMeasurement> findBySystemAndMeasurementType(final System systemToCheck,final QoSMeasurementType type);
}