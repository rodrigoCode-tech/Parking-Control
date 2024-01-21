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

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        assertEquals("Conflict:  Duplicate parking spot information!", result.getResponse().getContentAsString());
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
     public void should_Find_All_Parking_Spots() throws Exception {
         String parkingSpotJson = "{\"licensePlateCar\": \"ABC-369\", \"parkingSpotNumber\": \"A9\"," +
                 " \"responsibleName\": \"Diego\", \"brandCar\": \"Hunday\", \"modelCar\": \"Civic\"," +
                 " \"apartment\": \"768\", \"block\": \"c\", \"colorCar\": \"red\"}";

         mockMvc.perform(MockMvcRequestBuilders.post("/parkingSpot")
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(parkingSpotJson))
                 .andExpect(status().isCreated());

         mockMvc.perform(MockMvcRequestBuilders.get("/parkingSpot")
                         .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$.content", hasSize(greaterThan(0))));
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
                .andExpect(jsonPath("$.content[0].licensePlateCar").value(licensePlateCar))
                .andExpect(jsonPath("$.content[0].parkingSpotNumber").value("A7"))
                .andExpect(jsonPath("$.content[0].apartment").value("16"));
    }

    @Test
    public void should_update_parking_spot() throws Exception {
        String parkingSpotJson = "{\"licensePlateCar\": \"vfd-968\", \"parkingSpotNumber\": \"D5\"," +
                " \"responsibleName\": \"Edgar\", \"brandCar\": \"Fiat\", \"modelCar\": \"Toro\"," +
                " \"apartment\": \"154\", \"block\": \"F\", \"colorCar\": \"PINK\"}";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/parkingSpot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(parkingSpotJson))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        JsonNode jsonNode = new ObjectMapper().readTree(responseContent);
        UUID createdParkingSpotId = UUID.fromString(jsonNode.get("id").textValue());

        String updatedParkingSpotJson = "{\"licensePlateCar\": \"ABC789\", \"parkingSpotNumber\": \"B1\"," +
                " \"responsibleName\": \"UpdatedName\", \"brandCar\": \"UpdatedBrand\", \"modelCar\": \"UpdatedModel\"," +
                " \"apartment\": \"1234\", \"block\": \"E\", \"colorCar\": \"Black\"}";

        MvcResult mvcResultUpdate = mockMvc.perform(MockMvcRequestBuilders.put("/parkingSpot/" + createdParkingSpotId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedParkingSpotJson))
                .andExpect(status().isNoContent())
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get("/parkingSpot/" + createdParkingSpotId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdParkingSpotId.toString()))
                .andExpect(jsonPath("$.licensePlateCar").value("ABC789"))
                .andExpect(jsonPath("$.parkingSpotNumber").value("B1"))
                .andExpect(jsonPath("$.apartment").value("1234"))
                .andExpect(jsonPath("$.block").value("E"))
                .andExpect(jsonPath("$.brandCar").value("UpdatedBrand"))
                .andExpect(jsonPath("$.colorCar").value("Black"))
                .andExpect(jsonPath("$.modelCar").value("UpdatedModel"))
                .andExpect(jsonPath("$.responsibleName").value("UpdatedName"));
    }

    @Test
    public void should_fail_to_update_parking_spot() throws Exception {
        UUID parkingSpotId = UUID.randomUUID();

        String updatedParkingSpotJson = "{\"licensePlateCar\": \"ABC789\", \"parkingSpotNumber\": \"B1\"," +
                " \"responsibleName\": \"UpdatedName\", \"brandCar\": \"UpdatedBrand\", \"modelCar\": \"UpdatedModel\"," +
                " \"apartment\": \"1234\", \"block\": \"E\", \"colorCar\": \"Black\"}";

        mockMvc.perform(MockMvcRequestBuilders.put("/parkingSpot/" + parkingSpotId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedParkingSpotJson))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Parking Spot not found."));
    }
}