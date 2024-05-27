package infsus.pampol.repository;

import infsus.pampol.entity.Pharmacy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PharmacyRepositoryTest {

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @BeforeEach
    public void setUp() {
        pharmacyRepository.deleteAll();
    }

    @Test
    public void testSaveAndFindPharmacy() {
        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setName("Good Health Pharmacy");
        pharmacy.setAddress("789 Pine Street");

        pharmacyRepository.save(pharmacy);

        List<Pharmacy> pharmacies = pharmacyRepository.findAll();
        assertThat(pharmacies).hasSize(1).contains(pharmacy);
    }

}
