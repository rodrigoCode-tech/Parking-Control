package com.api.parkingcontrol.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.parkingcontrol.service.parkingSpotService;

@RestController
@CrossOrigin(origins = "*" , maxAge = 3600)
@RequestMapping("/parkingSpot")
public class parkingSpotController {

	@Autowired
	parkingSpotService service;
}
