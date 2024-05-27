package infsus.pampol.service;

import infsus.pampol.entity.Doctor;
import infsus.pampol.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

public class DoctorServiceTest {

    private DoctorService doctorService;

    private DoctorRepository doctorRepository;

    private Doctor doctor;

    @BeforeEach
    public void setup() {
        doctorRepository = Mockito.mock(DoctorRepository.class);
        doctorService = new DoctorService(doctorRepository);

        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setSpecialty("Cardiology");
    }

    @Test
    public void testGetAllDoctors() {
        Mockito.when(doctorRepository.findAll()).thenReturn(Arrays.asList(doctor));

        assertThat(doctorService.getAllDoctors()).hasSize(1).contains(doctor);
    }

    @Test
    public void testGetDoctorById() {
        Mockito.when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        Optional<Doctor> foundDoctor = doctorService.getDoctorById(1L);
        assertThat(foundDoctor).isPresent();
        assertThat(foundDoctor.get().getFirstName()).isEqualTo("John");
    }

    @Test
    public void testCreateDoctor() {
        Mockito.when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        Doctor createdDoctor = doctorService.createDoctor(doctor);
        assertThat(createdDoctor.getFirstName()).isEqualTo("John");
    }

    @Test
    public void testUpdateDoctor() {
        Mockito.when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        Mockito.when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        Doctor updatedDoctor = doctorService.updateDoctor(1L, doctor);
        assertThat(updatedDoctor.getFirstName()).isEqualTo("John");
    }

    @Test
    public void testDeleteDoctor() {
        doctorService.deleteDoctor(1L);
        Mockito.verify(doctorRepository, Mockito.times(1)).deleteById(1L);
    }
}
