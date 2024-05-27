package infsus.pampol.controller;

import infsus.pampol.dto.command.PharmacistCreateCommand;
import infsus.pampol.dto.command.PharmacistUpdateCommand;
import infsus.pampol.dto.response.PharmacistResponse;
import infsus.pampol.entity.Pharmacist;
import infsus.pampol.entity.Pharmacy;
import infsus.pampol.mapper.PharmacistMapper;
import infsus.pampol.service.PharmacistService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PharmacistController.class)
public class PharmacistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PharmacistService pharmacistService;

    @MockBean
    private PharmacistMapper pharmacistMapper;

    private Pharmacist pharmacist;
    private PharmacistResponse pharmacistResponse;

    @BeforeEach
    public void setup() {
        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setId(1L);
        pharmacy.setName("Good Health Pharmacy");
        pharmacy.setAddress("789 Pine Street");

        pharmacist = new Pharmacist();
        pharmacist.setId(1L);
        pharmacist.setFirstName("Clara");
        pharmacist.setLastName("Oswald");
        pharmacist.setPharmacy(pharmacy);

        pharmacistResponse = new PharmacistResponse();
        pharmacistResponse.setId(1L);
        pharmacistResponse.setFirstName("Clara");
        pharmacistResponse.setLastName("Oswald");
    }

    @Test
    public void testGetAllPharmacists() throws Exception {
        Mockito.when(pharmacistService.getAllPharmacists()).thenReturn(Arrays.asList(pharmacist));
        Mockito.when(pharmacistMapper.toResponse(any())).thenReturn(pharmacistResponse);

        mockMvc.perform(get("/api/pharmacists"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].firstName").value("Clara"))
            .andExpect(jsonPath("$[0].lastName").value("Oswald"));
    }

    @Test
    public void testGetPharmacistById() throws Exception {
        Mockito.when(pharmacistService.getPharmacistById(1L)).thenReturn(Optional.of(pharmacist));
        Mockito.when(pharmacistMapper.toResponse(any())).thenReturn(pharmacistResponse);

        mockMvc.perform(get("/api/pharmacists/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("Clara"))
            .andExpect(jsonPath("$.lastName").value("Oswald"));
    }

    @Test
    public void testCreatePharmacist() throws Exception {
        PharmacistCreateCommand command = new PharmacistCreateCommand();
        command.setFirstName("Clara");
        command.setLastName("Oswald");

        Mockito.when(pharmacistMapper.toEntity(any(PharmacistCreateCommand.class))).thenReturn(pharmacist);
        Mockito.when(pharmacistService.createPharmacist(any(Pharmacist.class))).thenReturn(pharmacist);
        Mockito.when(pharmacistMapper.toResponse(any())).thenReturn(pharmacistResponse);

        mockMvc.perform(post("/api/pharmacists")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"Clara\",\"lastName\":\"Oswald\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("Clara"))
            .andExpect(jsonPath("$.lastName").value("Oswald"));
    }

    @Test
    public void testUpdatePharmacist() throws Exception {
        PharmacistUpdateCommand command = new PharmacistUpdateCommand();
        command.setFirstName("Clara");
        command.setLastName("Oswald");

        Mockito.when(pharmacistService.updatePharmacist(eq(1L), any(Pharmacist.class))).thenReturn(pharmacist);
        Mockito.when(pharmacistMapper.toResponse(any())).thenReturn(pharmacistResponse);

        mockMvc.perform(put("/api/pharmacists/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"Clara\",\"lastName\":\"Oswald\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("Clara"))
            .andExpect(jsonPath("$.lastName").value("Oswald"));
    }

    @Test
    public void testDeletePharmacist() throws Exception {
        mockMvc.perform(delete("/api/pharmacists/1"))
            .andExpect(status().isNoContent());
    }

}
