package com.api.parkingcontrol.controller;

import com.api.parkingcontrol.service.ParkingSpotService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ParkingSpotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
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
        String parkingSpotJson = "{\"licensePlateCar\": \"ABC-123\", \"parkingSpotNumber\": \"A1\"," +
                " \"responsibleName\": \"Marcos\", \"brandCar\": \"Volkswagen\", \"modelCar\": \"Gol\"," +
                " \"apartment\": \"111\", \"block\": \"B\", \"colorCar\": \"Blue\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/parkingSpot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(parkingSpotJson))
                .andExpect(status().isCreated());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/parkingSpot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(parkingSpotJson))
                .andExpect(status().isConflict())
                .andReturn();

        assertEquals(409, result.getResponse().getStatus());
        assertEquals("Conflict: Conflict: Duplicate parking spot information!", result.getResponse().getContentAsString());
    }

        @Test
        public void should_Find_Parking_Spot_By_Id() throws Exception {
            String parkingSpotJson = "{\"licensePlateCar\": \"ABC-968\", \"parkingSpotNumber\": \"A8\"," +
                    " \"responsibleName\": \"Rafael\", \"brandCar\": \"Hunday\", \"modelCar\": \"hb20\"," +
                    " \"apartment\": \"165\", \"block\": \"c\", \"colorCar\": \"red\"}";

            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/parkingSpot")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(parkingSpotJson))
                    .andExpect(status().isCreated())
                    .andReturn();

            String responseContent = mvcResult.getResponse().getContentAsString();
            JsonNode jsonNode = new ObjectMapper().readTree(responseContent);
            UUID createdParkingSpotId = UUID.fromString(jsonNode.get("id").textValue());

            mockMvc.perform(MockMvcRequestBuilders.get("/parkingSpot/" + createdParkingSpotId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(createdParkingSpotId.toString()))
                    .andExpect(jsonPath("$.licensePlateCar").value("ABC-968"))
                    .andExpect(jsonPath("$.parkingSpotNumber").value("A8"))
                    .andExpect(jsonPath("$.apartment").value("165"))
                    .andExpect(jsonPath("$.block").value("c"))
                    .andExpect(jsonPath("$.brandCar").value("Hunday"))
                    .andExpect(jsonPath("$.colorCar").value("red"))
                    .andExpect(jsonPath("$.modelCar").value("hb20"))
                    .andExpect(jsonPath("$.responsibleName").value("Rafael"));

     }

     @Test
    public void should_Delete_Parking_Spot() throws Exception {
        String parkingSpotJson = "{\"licensePlateCar\": \"ABC-968\", \"parkingSpotNumber\": \"A8\"," +
                " \"responsibleName\": \"Rafael\", \"brandCar\": \"Hunday\", \"modelCar\": \"hb20\"," +
                " \"apartment\": \"165\", \"block\": \"c\", \"colorCar\": \"red\"}";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/parkingSpot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(parkingSpotJson))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        JsonNode jsonNode = new ObjectMapper().readTree(responseContent);
        UUID createdParkingSpotId = UUID.fromString(jsonNode.get("id").textValue());

        mockMvc.perform(MockMvcRequestBuilders.delete("/parkingSpot/" + createdParkingSpotId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/parkingSpot/" + createdParkingSpotId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_return_plate_car() throws Exception {
        String licensePlateCar = "ABC-954";
        String parkingSpotJson = "{\"licensePlateCar\": \"" + licensePlateCar + "\", \"parkingSpotNumber\": \"A7\"," +
                " \"responsibleName\": \"Karine\", \"brandCar\": \"Fiat\", \"modelCar\": \"Toro\"," +
                " \"apartment\": \"16\", \"block\": \"d\", \"colorCar\": \"red\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/parkingSpot")
                .contentType(MediaType.APPLICATION_JSON)
                .content(parkingSpotJson))
                .andExpect(status().isCreated())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get("/parkingSpot/name/" + licensePlateCar)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].licensePlateCar").value(licensePlateCar   ));
    }

}