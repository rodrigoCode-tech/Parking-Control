package com.api.parkingcontrol.service;

import com.api.parkingcontrol.dtos.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repository.ParkingSpotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ParkingSpotServiceTest {

    private final ParkingSpotRepository repository = mock(ParkingSpotRepository.class);
    private final ParkingSpotService service = new ParkingSpotService(repository);

    ParkingSpotModel model;

    @BeforeEach
    void setup() {
        model = new ParkingSpotModel();
        model.setParkingSpotNumber("10");
        model.setLicensePlateCar("ABC-1234");
        model.setBrandCar("Volkswagen");
        model.setModelCar("Gol");
        model.setColorCar("Blue");
        model.setResponsibleName("John Doe");
        model.setApartment("111");
        model.setBlock("B");
    }

    @Test
    void should_Save_a_Parking_Spot() {
        Mockito.when(repository.save(any(ParkingSpotModel.class))).thenReturn(model);

        ParkingSpotModel result = service.save(model);

        ArgumentCaptor<ParkingSpotModel> captor = ArgumentCaptor.forClass(ParkingSpotModel.class);
        verify(repository).save(captor.capture());

        ParkingSpotModel capturedModel = captor.getValue();
        assertEquals("10", capturedModel.getParkingSpotNumber());
        assertEquals("ABC-1234", capturedModel.getLicensePlateCar());
        assertEquals("Volkswagen", capturedModel.getBrandCar());
        assertEquals("Gol", capturedModel.getModelCar());
        assertEquals("Blue", capturedModel.getColorCar());
        assertEquals("John Doe", capturedModel.getResponsibleName());
        assertEquals("111", capturedModel.getApartment());
        assertEquals("B", capturedModel.getBlock());

        assertEquals(model, result);
    }

    @Test
    void should_ReturnTrue_When_DuplicateLicensePlateCarExists() {
        ParkingSpotDto parkingSpotDto = new ParkingSpotDto();
        parkingSpotDto.setLicensePlateCar("ABC-1234");

        Mockito.when(repository.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())).thenReturn(true);

        boolean result = service.existsDuplicateParkingSpot(parkingSpotDto);

        assertTrue(result);
    }

    @Test
    void should_ReturnTrue_When_DuplicateParkingSpotNumberExists() {
        ParkingSpotDto parkingSpotDto = new ParkingSpotDto();
        parkingSpotDto.setParkingSpotNumber("10");

        Mockito.when(repository.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())).thenReturn(true);

        boolean result = service.existsDuplicateParkingSpot(parkingSpotDto);

        assertTrue(result);
    }

    @Test
    void should_ReturnTrue_When_DuplicateApartmentAndBlockExist() {
        ParkingSpotDto parkingSpotDto = new ParkingSpotDto();
        parkingSpotDto.setApartment("111");
        parkingSpotDto.setBlock("B");

        Mockito.when(repository.existsByApartmentAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock())).thenReturn(true);

        boolean result = service.existsDuplicateParkingSpot(parkingSpotDto);

        assertTrue(result);
    }

    @Test
    void should_ReturnFalse_When_NoDuplicatesExist() {
        ParkingSpotDto parkingSpotDto = new ParkingSpotDto();
        parkingSpotDto.setLicensePlateCar("ABC-1234");
        parkingSpotDto.setParkingSpotNumber("10");
        parkingSpotDto.setApartment("111");
        parkingSpotDto.setBlock("B");

        Mockito.when(repository.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())).thenReturn(false);
        Mockito.when(repository.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())).thenReturn(false);
        Mockito.when(repository.existsByApartmentAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock())).thenReturn(false);

        boolean result = service.existsDuplicateParkingSpot(parkingSpotDto);

        assertFalse(result);
    }
}