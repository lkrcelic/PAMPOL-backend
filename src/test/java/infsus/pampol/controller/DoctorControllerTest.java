package infsus.pampol.controller;

import infsus.pampol.dto.command.DoctorCreateCommand;
import infsus.pampol.dto.command.DoctorUpdateCommand;
import infsus.pampol.dto.response.DoctorResponse;
import infsus.pampol.entity.Doctor;
import infsus.pampol.mapper.DoctorMapper;
import infsus.pampol.service.DoctorService;
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

@WebMvcTest(DoctorController.class)
public class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorService;

    @MockBean
    private DoctorMapper doctorMapper;

    private Doctor doctor;
    private DoctorResponse doctorResponse;

    @BeforeEach
    public void setup() {
        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setSpecialty("Cardiology");

        doctorResponse = new DoctorResponse();
        doctorResponse.setId(1L);
        doctorResponse.setFirstName("John");
        doctorResponse.setLastName("Doe");
        doctorResponse.setSpecialty("Cardiology");
    }

    @Test
    public void testGetAllDoctors() throws Exception {
        Mockito.when(doctorService.getAllDoctors()).thenReturn(Arrays.asList(doctor));
        Mockito.when(doctorMapper.toResponse(any())).thenReturn(doctorResponse);

        mockMvc.perform(get("/api/doctors"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].firstName").value("John"))
            .andExpect(jsonPath("$[0].lastName").value("Doe"))
            .andExpect(jsonPath("$[0].specialty").value("Cardiology"));
    }

    @Test
    public void testGetDoctorById() throws Exception {
        Mockito.when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(doctor));
        Mockito.when(doctorMapper.toResponse(any())).thenReturn(doctorResponse);

        mockMvc.perform(get("/api/doctors/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("John"))
            .andExpect(jsonPath("$.lastName").value("Doe"))
            .andExpect(jsonPath("$.specialty").value("Cardiology"));
    }

    @Test
    public void testCreateDoctor() throws Exception {
        DoctorCreateCommand command = new DoctorCreateCommand();
        command.setFirstName("John");
        command.setLastName("Doe");
        command.setSpecialty("Cardiology");

        Mockito.when(doctorMapper.toEntity(any(DoctorCreateCommand.class))).thenReturn(doctor);
        Mockito.when(doctorService.createDoctor(any(Doctor.class))).thenReturn(doctor);
        Mockito.when(doctorMapper.toResponse(any())).thenReturn(doctorResponse);

        mockMvc.perform(post("/api/doctors")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"specialty\":\"Cardiology\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("John"))
            .andExpect(jsonPath("$.lastName").value("Doe"))
            .andExpect(jsonPath("$.specialty").value("Cardiology"));
    }

    @Test
    public void testUpdateDoctor() throws Exception {
        DoctorUpdateCommand command = new DoctorUpdateCommand();
        command.setFirstName("John");
        command.setLastName("Doe");
        command.setSpecialty("Cardiology");

        Mockito.when(doctorService.updateDoctor(eq(1L), any(Doctor.class))).thenReturn(doctor);
        Mockito.when(doctorMapper.toResponse(any())).thenReturn(doctorResponse);

        mockMvc.perform(put("/api/doctors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"specialty\":\"Cardiology\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("John"))
            .andExpect(jsonPath("$.lastName").value("Doe"))
            .andExpect(jsonPath("$.specialty").value("Cardiology"));
    }

    @Test
    public void testDeleteDoctor() throws Exception {
        mockMvc.perform(delete("/api/doctors/1"))
            .andExpect(status().isNoContent());
    }
}
