package infsus.pampol.repository;

import infsus.pampol.entity.Pharmacist;
import infsus.pampol.entity.Pharmacy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PharmacistRepositoryTest {

    @Autowired
    private PharmacistRepository pharmacistRepository;

    @BeforeEach
    public void setUp() {
        pharmacistRepository.deleteAll();
    }

    @Test
    public void testSaveAndFindPharmacist() {
        Pharmacist pharmacist = new Pharmacist();
        pharmacist.setFirstName("Clara");
        pharmacist.setLastName("Oswald");

        pharmacistRepository.save(pharmacist);

        List<Pharmacist> pharmacists = pharmacistRepository.findAll();
        assertThat(pharmacists).hasSize(1).contains(pharmacist);
    }

}
