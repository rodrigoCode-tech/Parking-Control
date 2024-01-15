package com.api.parkingcontrol.service;

import com.api.parkingcontrol.dtos.ParkingSpotDto;
import com.api.parkingcontrol.exception.DuplicateParkingSpotException;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repository.ParkingSpotRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParkingSpotService {

	//Injeção de dependêcias
	private final ParkingSpotRepository repository;

	public ParkingSpotService(ParkingSpotRepository repository) {
		this.repository = repository;
	}

	@Transactional
    public ParkingSpotModel save(ParkingSpotModel parkingSpotModel) {
		if (existsDuplicateParkingSpotModel(parkingSpotModel)) {
			throw new DuplicateParkingSpotException("Conflict: Duplicate parking spot information!");
		}
		return repository.save(parkingSpotModel);
	}
	public boolean existsDuplicateParkingSpotModel(ParkingSpotModel parkingSpotModel) {
		return repository.existsByLicensePlateCar(parkingSpotModel.getLicensePlateCar()) ||
				repository.existsByParkingSpotNumber(parkingSpotModel.getParkingSpotNumber()) ||
				repository.existsByApartmentAndBlock(parkingSpotModel.getApartment(), parkingSpotModel.getBlock());
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

	public ParkingSpotModel update(UUID id, ParkingSpotDto parkingSpotDto) {
		Optional<ParkingSpotModel> parkingSpotModelOptional = repository.findById(id);
		if (!parkingSpotModelOptional.isPresent()) {
			throw new DuplicateParkingSpotException("Conflict: Duplicate parking spot information!");
		}

		ParkingSpotModel existingParkingSpotModel = parkingSpotModelOptional.get();
		BeanUtils.copyProperties(parkingSpotDto, existingParkingSpotModel);
		return repository.save(existingParkingSpotModel);
	}
	
	
	
}
