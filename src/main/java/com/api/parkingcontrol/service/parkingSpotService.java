package com.api.parkingcontrol.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.parkingcontrol.repository.parkingSpotRepository;

@Service
public class parkingSpotService {

	//Injeção de dependêcias
	@Autowired
	parkingSpotRepository repository;
	
	
	
}
