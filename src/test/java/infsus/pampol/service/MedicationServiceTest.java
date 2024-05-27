package infsus.pampol.service;

import infsus.pampol.entity.Medication;
import infsus.pampol.repository.MedicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MedicationServiceTest {

    private MedicationRepository medicationRepository;
    private MedicationService medicationService;

    @BeforeEach
    public void setUp() {
        medicationRepository = Mockito.mock(MedicationRepository.class);
        medicationService = new MedicationService(medicationRepository);

        Medication medication = new Medication();
        medication.setId(1L);
        medication.setName("Ibuprofen");
        medication.setManufacturer("Over-The-Counter Meds");
        medication.setPrice(8.49);

        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));
        when(medicationRepository.save(any(Medication.class))).thenReturn(medication);
    }

    @Test
    public void testGetMedicationById() {
        Optional<Medication> medication = medicationService.getMedicationById(1L);
        assertThat(medication).isPresent();
        assertThat(medication.get().getName()).isEqualTo("Ibuprofen");
    }

    @Test
    public void testCreateMedication() {
        Medication medication = new Medication();
        medication.setName("Paracetamol");
        medication.setManufacturer("Generic Pharmaceuticals");
        medication.setPrice(5.99);

        Medication createdMedication = medicationService.createMedication(medication);
        assertThat(createdMedication).isNotNull();
        assertThat(createdMedication.getName()).isEqualTo("Ibuprofen");
    }

}
