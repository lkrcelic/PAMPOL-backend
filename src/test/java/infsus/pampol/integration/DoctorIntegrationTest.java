package infsus.pampol.integration;

import infsus.pampol.dto.command.DoctorCreateCommand;
import infsus.pampol.dto.response.DoctorResponse;
import infsus.pampol.entity.Doctor;
import infsus.pampol.repository.DoctorRepository;
import infsus.pampol.service.DoctorService;
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
public class DoctorIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setSpecialty("Cardiology");
        doctorRepository.save(doctor);
    }

    @Test
    public void testGetDoctorById() throws Exception {
        Doctor doctor = doctorRepository.findAll().get(0);
        MvcResult result = mockMvc.perform(get("/api/doctors/" + doctor.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        String content = result.getResponse().getContentAsString();
        DoctorResponse response = modelMapper.map(doctor, DoctorResponse.class);
        assertThat(content).contains(response.getFirstName());
        assertThat(content).contains(response.getLastName());
        assertThat(content).contains(response.getSpecialty());
    }

    @Test
    public void testCreateDoctor() throws Exception {
        DoctorCreateCommand command = new DoctorCreateCommand();
        command.setFirstName("Jane");
        command.setLastName("Smith");
        command.setSpecialty("Neurology");

        MvcResult result = mockMvc.perform(post("/api/doctors")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"firstName\": \"Jane\", \"lastName\": \"Smith\", \"specialty\": \"Neurology\" }"))
            .andExpect(status().isOk())
            .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("Jane");
        assertThat(content).contains("Smith");
        assertThat(content).contains("Neurology");
    }

}
