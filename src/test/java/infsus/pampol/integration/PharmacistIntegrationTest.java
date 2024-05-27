package infsus.pampol.integration;

import infsus.pampol.dto.command.PharmacistCreateCommand;
import infsus.pampol.dto.response.PharmacistResponse;
import infsus.pampol.entity.Pharmacist;
import infsus.pampol.entity.Pharmacy;
import infsus.pampol.repository.PharmacistRepository;
import infsus.pampol.repository.PharmacyRepository;
import infsus.pampol.service.PharmacistService;
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
public class PharmacistIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PharmacistRepository pharmacistRepository;

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private PharmacistService pharmacistService;

    @Autowired
    private ModelMapper modelMapper;

    private Pharmacy pharmacy;

    @BeforeEach
    public void setUp() {
        pharmacy = new Pharmacy();
        pharmacy.setName("Good Health Pharmacy");
        pharmacy.setAddress("789 Pine Street");
        pharmacyRepository.save(pharmacy);

        Pharmacist pharmacist = new Pharmacist();
        pharmacist.setFirstName("Clara");
        pharmacist.setLastName("Oswald");
        pharmacist.setPharmacy(pharmacy);
        pharmacistRepository.save(pharmacist);
    }

    @Test
    public void testGetPharmacistById() throws Exception {
        Pharmacist pharmacist = pharmacistRepository.findAll().get(0);
        MvcResult result = mockMvc.perform(get("/api/pharmacists/" + pharmacist.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        String content = result.getResponse().getContentAsString();
        PharmacistResponse response = modelMapper.map(pharmacist, PharmacistResponse.class);
        assertThat(content).contains(response.getFirstName());
        assertThat(content).contains(response.getLastName());
    }

    @Test
    public void testCreatePharmacist() throws Exception {
        PharmacistCreateCommand command = new PharmacistCreateCommand();
        command.setFirstName("Bruce");
        command.setLastName("Wayne");

        MvcResult result = mockMvc.perform(post("/api/pharmacists?pharmacyId=" + pharmacy.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"firstName\": \"Bruce\", \"lastName\": \"Wayne\" }"))
            .andExpect(status().isOk())
            .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("Bruce");
        assertThat(content).contains("Wayne");
    }

}
