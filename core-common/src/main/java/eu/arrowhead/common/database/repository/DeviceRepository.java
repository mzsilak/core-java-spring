package eu.arrowhead.common.database.repository;

import eu.arrowhead.common.database.entity.Device;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends RefreshableRepository<Device,Long> {
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public Optional<Device> findByDeviceNameAndMacAddress(final String deviceName, final String address);
	public List<Device> findByDeviceName(final String deviceName);
}