package infsus.pampol.controller;

import infsus.pampol.dto.command.MedicationCreateCommand;
import infsus.pampol.dto.command.MedicationUpdateCommand;
import infsus.pampol.dto.response.MedicationResponse;
import infsus.pampol.entity.Medication;
import infsus.pampol.mapper.MedicationMapper;
import infsus.pampol.service.MedicationService;
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

@WebMvcTest(MedicationController.class)
public class MedicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicationService medicationService;

    @MockBean
    private MedicationMapper medicationMapper;

    private Medication medication;
    private MedicationResponse medicationResponse;

    @BeforeEach
    public void setup() {
        medication = new Medication();
        medication.setId(1L);
        medication.setName("Ibuprofen");
        medication.setManufacturer("Over-The-Counter Meds");
        medication.setPrice(8.49);

        medicationResponse = new MedicationResponse();
        medicationResponse.setId(1L);
        medicationResponse.setName("Ibuprofen");
        medicationResponse.setManufacturer("Over-The-Counter Meds");
        medicationResponse.setPrice(8.49);
    }

    @Test
    public void testGetAllMedications() throws Exception {
        Mockito.when(medicationService.getAllMedications()).thenReturn(Arrays.asList(medication));
        Mockito.when(medicationMapper.toResponse(any())).thenReturn(medicationResponse);

        mockMvc.perform(get("/api/medications"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Ibuprofen"))
            .andExpect(jsonPath("$[0].manufacturer").value("Over-The-Counter Meds"))
            .andExpect(jsonPath("$[0].price").value(8.49));
    }

    @Test
    public void testGetMedicationById() throws Exception {
        Mockito.when(medicationService.getMedicationById(1L)).thenReturn(Optional.of(medication));
        Mockito.when(medicationMapper.toResponse(any())).thenReturn(medicationResponse);

        mockMvc.perform(get("/api/medications/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Ibuprofen"))
            .andExpect(jsonPath("$.manufacturer").value("Over-The-Counter Meds"))
            .andExpect(jsonPath("$.price").value(8.49));
    }

    @Test
    public void testCreateMedication() throws Exception {
        MedicationCreateCommand command = new MedicationCreateCommand();
        command.setName("Ibuprofen");
        command.setManufacturer("Over-The-Counter Meds");
        command.setPrice(8.49);

        Mockito.when(medicationMapper.toEntity(any(MedicationCreateCommand.class))).thenReturn(medication);
        Mockito.when(medicationService.createMedication(any(Medication.class))).thenReturn(medication);
        Mockito.when(medicationMapper.toResponse(any())).thenReturn(medicationResponse);

        mockMvc.perform(post("/api/medications")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Ibuprofen\",\"manufacturer\":\"Over-The-Counter Meds\",\"price\":8.49}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Ibuprofen"))
            .andExpect(jsonPath("$.manufacturer").value("Over-The-Counter Meds"))
            .andExpect(jsonPath("$.price").value(8.49));
    }

    @Test
    public void testUpdateMedication() throws Exception {
        MedicationUpdateCommand command = new MedicationUpdateCommand();
        command.setName("Ibuprofen");
        command.setManufacturer("Over-The-Counter Meds");
        command.setPrice(8.49);

        Mockito.when(medicationService.updateMedication(eq(1L), any(Medication.class))).thenReturn(medication);
        Mockito.when(medicationMapper.toResponse(any())).thenReturn(medicationResponse);

        mockMvc.perform(put("/api/medications/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Ibuprofen\",\"manufacturer\":\"Over-The-Counter Meds\",\"price\":8.49}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Ibuprofen"))
            .andExpect(jsonPath("$.manufacturer").value("Over-The-Counter Meds"))
            .andExpect(jsonPath("$.price").value(8.49));
    }

    @Test
    public void testDeleteMedication() throws Exception {
        mockMvc.perform(delete("/api/medications/1"))
            .andExpect(status().isNoContent());
    }

}
