package com.api.parkingcontrol.service;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repository.ParkingSpotRepository;

@Service
public class ParkingSpotService {

	private final ParkingSpotRepository repository;

	public ParkingSpotService(ParkingSpotRepository repository) {
		this.repository = repository;
	}

	@Transactional
    public ParkingSpotModel save(ParkingSpotModel parkingSpotModel) {
		return repository.save(parkingSpotModel);
       
    }

	public  boolean existsByLicensePlateCar(String licensePlateCar) {
		return repository.existsByLicensePlateCar(licensePlateCar);
	}

	public  boolean existsByParkingSpotNumber(String parkingSpotNumber) {
		return repository.existsByParkingSpotNumber(parkingSpotNumber);
	}

	public  boolean existsByApartmentAndBlock(String apartment, String block) {
		return repository.existsByApartmentAndBlock(apartment, block);
	}

	public Page<ParkingSpotModel> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}
	
	public Page<ParkingSpotModel> findByName(String licensePlateCar,Pageable pageable){	
		
		licensePlateCar = licensePlateCar.isEmpty() ? null : licensePlateCar;
		return repository.findByName(licensePlateCar, pageable);
	}
	public Optional<ParkingSpotModel> findById(UUID id) {
        return repository.findById(id);
    }
	public  void delete(ParkingSpotModel parkingSpotModel) {
		repository.delete(parkingSpotModel);

	}
	
	
	
}
