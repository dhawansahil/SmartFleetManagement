package com.VehicleSvc.helper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.VehicleSvc.Repository.VehicleSvcRepository;
import com.VehicleSvc.to.Vehicle;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class VehicleSvcHelper {

	@Autowired
	private VehicleSvcRepository vehicleSvcRepository;

	public ResponseEntity<Vehicle> addVehicle(Vehicle vehicle) {

		return new ResponseEntity<>(vehicleSvcRepository.save(vehicle), HttpStatus.OK);

	}

	public ResponseEntity<Vehicle> retrieveVehicle(String vehicleNumber) {

		return new ResponseEntity<>(vehicleSvcRepository.findByVehicleNumber(vehicleNumber), HttpStatus.OK);

	}

	
	public ResponseEntity<List<Vehicle>> retrieveFleetInfo(Long fleetId){
		return new ResponseEntity<>(vehicleSvcRepository.findByFleetId(fleetId), HttpStatus.OK);

	}
}
