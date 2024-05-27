package infsus.pampol.service;

import infsus.pampol.entity.Pharmacist;
import infsus.pampol.entity.Pharmacy;
import infsus.pampol.repository.PharmacistRepository;
import infsus.pampol.repository.PharmacyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PharmacistServiceTest {

    private PharmacistRepository pharmacistRepository;
    private PharmacyRepository pharmacyRepository;
    private PharmacistService pharmacistService;

    @BeforeEach
    public void setUp() {
        pharmacistRepository = Mockito.mock(PharmacistRepository.class);
        pharmacyRepository = Mockito.mock(PharmacyRepository.class);
        pharmacistService = new PharmacistService(pharmacistRepository);

        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setId(1L);
        pharmacy.setName("Good Health Pharmacy");
        pharmacy.setAddress("789 Pine Street");

        Pharmacist pharmacist = new Pharmacist();
        pharmacist.setId(1L);
        pharmacist.setFirstName("Clara");
        pharmacist.setLastName("Oswald");
        pharmacist.setPharmacy(pharmacy);

        when(pharmacyRepository.findById(1L)).thenReturn(Optional.of(pharmacy));
        when(pharmacistRepository.findById(1L)).thenReturn(Optional.of(pharmacist));
        when(pharmacistRepository.save(any(Pharmacist.class))).thenReturn(pharmacist);
    }

    @Test
    public void testGetPharmacistById() {
        Optional<Pharmacist> pharmacist = pharmacistService.getPharmacistById(1L);
        assertThat(pharmacist).isPresent();
        assertThat(pharmacist.get().getFirstName()).isEqualTo("Clara");
    }

    @Test
    public void testCreatePharmacist() {
        Pharmacist pharmacist = new Pharmacist();
        pharmacist.setFirstName("Bruce");
        pharmacist.setLastName("Wayne");

        Pharmacist createdPharmacist = pharmacistService.createPharmacist(pharmacist);
        assertThat(createdPharmacist).isNotNull();
        assertThat(createdPharmacist.getFirstName()).isEqualTo("Clara");
    }

}
