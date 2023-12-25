package com.api.parkingcontrol.controller;

import com.api.parkingcontrol.service.ParkingSpotService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ParkingSpotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ParkingSpotService parkingSpotService;

    @Test
    public void should_Save_a_Parking_Spot() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/parkingSpot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"licensePlateCar\": \"ABC-754\", \"parkingSpotNumber\": \"A4\"," +
                                " \"responsibleName\": \"Marcio Rodrigo\", \"brandCar\": \"Volkswagen\", \"modelCar\": \"Nivus\"," +
                                " \"apartment\": \"759\", \"block\": \"D\", \"colorCar\": \"Red\"}"))
                .andExpect(status().isCreated())
                .andReturn();

        assertEquals(201, result.getResponse().getStatus());
    }

    @Test
    public void should_Not_Save_a_Parking_Spot_When_Exists_Duplicate_Informations() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/parkingSpot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"licensePlateCar\": \"ABC-123\", \"parkingSpotNumber\": \"A1\"," +
                                " \"responsibleName\": \"Marcos\", \"brandCar\": \"Volkswagen\", \"modelCar\": \"Gol\"," +
                                " \"apartment\": \"111\", \"block\": \"B\", \"colorCar\": \"Blue\"}"))
                .andExpect(status().isCreated());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/parkingSpot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"licensePlateCar\": \"ABC-123\", \"parkingSpotNumber\": \"A1\"," +
                                " \"responsibleName\": \"Marcos\", \"brandCar\": \"Volkswagen\", \"modelCar\": \"Gol\"," +
                                " \"apartment\": \"111\", \"block\": \"B\", \"colorCar\": \"Blue\"}"))
                .andExpect(status().isConflict())
                .andReturn();

        assertEquals(409, result.getResponse().getStatus());
        assertEquals("Conflict: Conflict: Duplicate parking spot information!", result.getResponse().getContentAsString());
    }
}