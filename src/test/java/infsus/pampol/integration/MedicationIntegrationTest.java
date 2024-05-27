package infsus.pampol.integration;

import infsus.pampol.dto.command.MedicationCreateCommand;
import infsus.pampol.dto.response.MedicationResponse;
import infsus.pampol.entity.Medication;
import infsus.pampol.repository.MedicationRepository;
import infsus.pampol.service.MedicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MedicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MedicationRepository medicationRepository;

    @Autowired
    private MedicationService medicationService;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        Medication medication = new Medication();
        medication.setName("Ibuprofen");
        medication.setManufacturer("Over-The-Counter Meds");
        medication.setPrice(8.49);
        medicationRepository.save(medication);
    }

    @Test
    public void testGetMedicationById() throws Exception {
        Medication medication = medicationRepository.findAll().get(0);
        MvcResult result = mockMvc.perform(get("/api/medications/" + medication.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        String content = result.getResponse().getContentAsString();
        MedicationResponse response = modelMapper.map(medication, MedicationResponse.class);
        assertThat(content).contains(response.getName());
        assertThat(content).contains(response.getManufacturer());
        assertThat(content).contains(String.valueOf(response.getPrice()));
    }

    @Test
    public void testCreateMedication() throws Exception {
        MedicationCreateCommand command = new MedicationCreateCommand();
        command.setName("Paracetamol");
        command.setManufacturer("Generic Pharmaceuticals");
        command.setPrice(5.99);

        MvcResult result = mockMvc.perform(post("/api/medications")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"Paracetamol\", \"manufacturer\": \"Generic Pharmaceuticals\", \"price\": 5.99 }"))
            .andExpect(status().isOk())
            .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("Paracetamol");
        assertThat(content).contains("Generic Pharmaceuticals");
        assertThat(content).contains("5.99");
    }

}
