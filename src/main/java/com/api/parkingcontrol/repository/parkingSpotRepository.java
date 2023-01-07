package com.api.parkingcontrol.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.parkingcontrol.models.ParkingSpotModel;

@Repository
public interface parkingSpotRepository extends JpaRepository<ParkingSpotModel, UUID>{

	//Como esses métodos são metodos customizados, para que eu os use
	//devo os declarar dentro do repository 
	boolean existsByLicensePlateCar(String licensePlateCar);
	boolean existsByParkingSpotNumber(String parkingSpotNumber);
	boolean existsByApartmentAndBlock(String apartment, String block);
	
	@Query(value="SELECT u FROM ParkingSpotModel u where u.licensePlateCar = ?1 OR ?1 is null")
	Page<ParkingSpotModel> findByName(@Param("licensePlateCar") String licensePlateCar, Pageable pageable);

}
