package infsus.pampol.repository;

import infsus.pampol.entity.Medication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MedicationRepositoryTest {

    @Autowired
    private MedicationRepository medicationRepository;

    @BeforeEach
    public void setUp() {
        medicationRepository.deleteAll();
    }

    @Test
    public void testSaveAndFindMedication() {
        Medication medication = new Medication();
        medication.setName("Ibuprofen");
        medication.setManufacturer("Over-The-Counter Meds");
        medication.setPrice(8.49);

        medicationRepository.save(medication);

        List<Medication> medications = medicationRepository.findAll();
        assertThat(medications).hasSize(1).contains(medication);
    }

}
