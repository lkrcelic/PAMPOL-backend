package infsus.pampol.controller;

import infsus.pampol.dto.command.PharmacyCreateCommand;
import infsus.pampol.dto.command.PharmacyUpdateCommand;
import infsus.pampol.dto.response.PharmacyResponse;
import infsus.pampol.entity.Pharmacy;
import infsus.pampol.mapper.PharmacyMapper;
import infsus.pampol.service.PharmacyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PharmacyController.class)
public class PharmacyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PharmacyService pharmacyService;

    @MockBean
    private PharmacyMapper pharmacyMapper;

    private Pharmacy pharmacy;
    private PharmacyResponse pharmacyResponse;

    @BeforeEach
    public void setup() {
        pharmacy = new Pharmacy();
        pharmacy.setId(1L);
        pharmacy.setName("Good Health Pharmacy");
        pharmacy.setAddress("789 Pine Street");

        pharmacyResponse = new PharmacyResponse();
        pharmacyResponse.setId(1L);
        pharmacyResponse.setName("Good Health Pharmacy");
        pharmacyResponse.setAddress("789 Pine Street");
    }

    @Test
    public void testGetAllPharmacies() throws Exception {
        Mockito.when(pharmacyService.getAllPharmacies()).thenReturn(Arrays.asList(pharmacyResponse));

        mockMvc.perform(get("/api/pharmacies"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Good Health Pharmacy"))
            .andExpect(jsonPath("$[0].address").value("789 Pine Street"));
    }

    @Test
    public void testGetPharmacyById() throws Exception {
        Mockito.when(pharmacyService.getPharmacyById(1L)).thenReturn(Optional.of(pharmacyResponse));

        mockMvc.perform(get("/api/pharmacies/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Good Health Pharmacy"))
            .andExpect(jsonPath("$.address").value("789 Pine Street"));
    }

    @Test
    public void testCreatePharmacy() throws Exception {
        PharmacyCreateCommand command = new PharmacyCreateCommand();
        command.setName("Good Health Pharmacy");
        command.setAddress("789 Pine Street");

        Mockito.when(pharmacyService.createPharmacy(any(PharmacyCreateCommand.class))).thenReturn(pharmacyResponse);

        mockMvc.perform(post("/api/pharmacies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Good Health Pharmacy\",\"address\":\"789 Pine Street\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Good Health Pharmacy"))
            .andExpect(jsonPath("$.address").value("789 Pine Street"));
    }

    @Test
    public void testUpdatePharmacy() throws Exception {
        PharmacyUpdateCommand command = new PharmacyUpdateCommand();
        command.setName("Good Health Pharmacy");
        command.setAddress("789 Pine Street");

        Mockito.when(pharmacyService.updatePharmacy(eq(1L), any(PharmacyUpdateCommand.class)))
            .thenReturn(pharmacyResponse);

        mockMvc.perform(put("/api/pharmacies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Good Health Pharmacy\",\"address\":\"789 Pine Street\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Good Health Pharmacy"))
            .andExpect(jsonPath("$.address").value("789 Pine Street"));
    }

    @Test
    public void testDeletePharmacy() throws Exception {
        mockMvc.perform(delete("/api/pharmacies/1"))
            .andExpect(status().isNoContent());
    }

}
