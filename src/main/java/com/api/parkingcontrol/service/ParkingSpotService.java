package com.api.parkingcontrol.service;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import com.api.parkingcontrol.dtos.ParkingSpotDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repository.ParkingSpotRepository;

@Service
public class ParkingSpotService {

	//Injeção de dependêcias
	private final ParkingSpotRepository repository;

	public ParkingSpotService(ParkingSpotRepository repository) {
		this.repository = repository;
	}

	@Transactional
    public ParkingSpotModel save(ParkingSpotModel parkingSpotModel) {
		return repository.save(parkingSpotModel);
    }

	public boolean existsDuplicateParkingSpot(ParkingSpotDto parkingSpotDto) {
		return repository.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar()) ||
				repository.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber()) ||
				repository.existsByApartmentAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock());
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
