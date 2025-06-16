package com.VehicleSvc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.VehicleSvc.helper.VehicleSvcHelper;
import com.VehicleSvc.to.Vehicle;



@RestController
@RequestMapping("/vehicle")
public class VehcileSvcController {

	@Autowired
	private VehicleSvcHelper vehicleSvcHelper;
	
	@PostMapping("/Vehicle")
	public ResponseEntity<Vehicle> addVehicle(@RequestBody Vehicle vehicle) {
		 return vehicleSvcHelper.addVehicle(vehicle);
	}
	
	//retrieveVehicle
	@GetMapping("/Vehicle")
	public ResponseEntity<Vehicle> retrieveVehicle(@RequestParam String VehicleNumber) {
		return vehicleSvcHelper.retrieveVehicle(VehicleNumber);
	}
	
	
	//update Vehicle
	
	//retrieve Fleet Info
	@GetMapping("/fleet")
	public  ResponseEntity<List<Vehicle>> retrieveFleetInfo(@RequestParam Long fleetId) {
		return vehicleSvcHelper.retrieveFleetInfo(fleetId);
	}
	
	
	//Delete Vehicle
	
}
