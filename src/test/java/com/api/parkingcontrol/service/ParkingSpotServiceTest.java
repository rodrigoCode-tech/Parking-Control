package com.api.parkingcontrol.service;

import com.api.parkingcontrol.dtos.ParkingSpotDto;
import com.api.parkingcontrol.exception.DuplicateParkingSpotException;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repository.ParkingSpotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        when(repository.save(any(ParkingSpotModel.class))).thenReturn(model);

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
    public void should_Return_Duplicate_Exception_When_DuplicateLicensePlateCarExists() {
        when(repository.existsByLicensePlateCar(model.getLicensePlateCar())).thenReturn(true);
        ParkingSpotService service = new ParkingSpotService(repository);

        assertThrows(DuplicateParkingSpotException.class, () -> service.save(model));
    }

    @Test
    public void should_Find_All_Parking_Spots_By_Page() {
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        Page<ParkingSpotModel> mockPage = new PageImpl<>(Collections.emptyList());

        when(repository.findAll(pageable)).thenReturn(mockPage);
        ParkingSpotService service = new ParkingSpotService(repository);

        Page<ParkingSpotModel> result = service.findAll(pageable);
        assertEquals(mockPage, result);
    }

    @Test
    public void should_Find_Parking_Spots_By_Name() {
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        Page<ParkingSpotModel> mockPage = new PageImpl<>(Collections.emptyList());

        when(repository.findByName("ABC-1234", pageable)).thenReturn(mockPage);
        ParkingSpotService service = new ParkingSpotService(repository);

        Page<ParkingSpotModel> result = service.findByName("ABC-1234", pageable);
        assertEquals(mockPage, result);
    }

    @Test
    public void should_Find_Parking_Spot_By_Id() {
        when(repository.findById(any())).thenReturn(java.util.Optional.of(model));
        ParkingSpotService service = new ParkingSpotService(repository);

        Optional<ParkingSpotModel> result = service.findById(model.getId());
        assertEquals(model, result.get());
    }

    @Test
    public void should_Delete_Parking_Spot() {
        doNothing().when(repository).delete(model);
        ParkingSpotService service = new ParkingSpotService(repository);

        service.delete(model);

        assertEquals(model, model);
    }

    @Test
    void should_ReturnTrue_When_DuplicateLicensePlateCarExists() {
        ParkingSpotModel parkingSpotModel = new ParkingSpotModel();
        parkingSpotModel.setLicensePlateCar("ABC-1234");

        when(repository.existsByLicensePlateCar(parkingSpotModel.getLicensePlateCar())).thenReturn(true);

        boolean result = service.existsDuplicateParkingSpotModel(parkingSpotModel);

        assertTrue(result);
    }

    @Test
    void should_ReturnTrue_When_DuplicateParkingSpotNumberExists() {
        ParkingSpotModel parkingSpotModel = new ParkingSpotModel();
        parkingSpotModel.setParkingSpotNumber("10");

        when(repository.existsByParkingSpotNumber(parkingSpotModel.getParkingSpotNumber())).thenReturn(true);

        boolean result = service.existsDuplicateParkingSpotModel(parkingSpotModel);

        assertTrue(result);
    }

    @Test
    void should_ReturnTrue_When_DuplicateApartmentAndBlockExist() {
        ParkingSpotModel parkingSpotModel = new ParkingSpotModel();
        parkingSpotModel.setApartment("111");
        parkingSpotModel.setBlock("B");

        when(repository.existsByApartmentAndBlock(parkingSpotModel.getApartment(), parkingSpotModel.getBlock())).thenReturn(true);

        boolean result = service.existsDuplicateParkingSpotModel(parkingSpotModel);

        assertTrue(result);
    }

    @Test
    void should_ReturnFalse_When_NoDuplicatesExist() {
        ParkingSpotModel parkingSpotModel = new ParkingSpotModel();
        parkingSpotModel.setLicensePlateCar("ABC-1234");
        parkingSpotModel.setParkingSpotNumber("10");
        parkingSpotModel.setApartment("111");
        parkingSpotModel.setBlock("B");

        when(repository.existsByLicensePlateCar(parkingSpotModel.getLicensePlateCar())).thenReturn(false);
        when(repository.existsByParkingSpotNumber(parkingSpotModel.getParkingSpotNumber())).thenReturn(false);
        when(repository.existsByApartmentAndBlock(parkingSpotModel.getApartment(), parkingSpotModel.getBlock())).thenReturn(false);

        boolean result = service.existsDuplicateParkingSpotModel(parkingSpotModel);

        assertFalse(result);
    }

    @Test
    void should_Update_ParkingSpot() {
        UUID id = UUID.randomUUID();
        ParkingSpotDto parkingSpotDto = new ParkingSpotDto();
        parkingSpotDto.setLicensePlateCar("ABC-1234");

        ParkingSpotModel existingModel = new ParkingSpotModel();
        existingModel.setId(id);
        existingModel.setLicensePlateCar("ABC-1232");

        when(repository.findById(id)).thenReturn(Optional.of(existingModel));
        when(repository.save(any(ParkingSpotModel.class))).thenReturn(existingModel);

        ParkingSpotModel result = service.update(id, parkingSpotDto);

        assertNotNull(result);
        assertEquals(existingModel, result);
        assertEquals("ABC-1234", existingModel.getLicensePlateCar());

        verify(repository, times(1)).save(any(ParkingSpotModel.class));
    }

    @Test
    void should_Do_not_Update_ParkingSpot_when_not_found() {
        UUID id = UUID.randomUUID();
        ParkingSpotDto parkingSpotDto = new ParkingSpotDto();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DuplicateParkingSpotException.class, () -> service.update(id, parkingSpotDto));

        verify(repository, times(1)).findById(id);
        verify(repository, never()).save(any(ParkingSpotModel.class));
    }
}