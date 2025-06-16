package com.VehicleSvc.to;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Vehicle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long vehicleId;
	@Nullable
	private Long fleetId;
	private String vehicleNumber;
	private String vehicleType;
	private String vehicleName;
	private int vehicleLoad;

}
