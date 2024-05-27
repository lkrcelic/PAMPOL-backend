package infsus.pampol.repository;

import infsus.pampol.entity.PharmacyMedication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PharmacyMedicationRepository extends JpaRepository<PharmacyMedication, Long> {

}
