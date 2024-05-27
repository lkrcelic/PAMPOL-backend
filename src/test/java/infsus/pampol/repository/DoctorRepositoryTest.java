package infsus.pampol.repository;

import infsus.pampol.entity.Doctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DoctorRepositoryTest {

    @Autowired
    private DoctorRepository doctorRepository;

    @BeforeEach
    public void setUp() {
        doctorRepository.deleteAll();
    }

    @Test
    public void testSaveAndFindDoctor() {
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setSpecialty("Cardiology");

        doctorRepository.save(doctor);

        List<Doctor> doctors = doctorRepository.findAll();
        assertThat(doctors).hasSize(1).contains(doctor);
    }
}
