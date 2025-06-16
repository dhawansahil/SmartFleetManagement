package com.VehicleSvc.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.VehicleSvc.to.Vehicle;

@Repository
public interface VehicleSvcRepository extends JpaRepository<Vehicle, Long> {

	public Vehicle save(Vehicle vehicle);
	
	public Vehicle findByVehicleNumber(String vehicleNumber);
	
	public List<Vehicle> findByFleetId(Long fleetId);
	
}
