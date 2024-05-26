package infsus.pampol.service;

import infsus.pampol.entity.Medication;
import infsus.pampol.repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;

    @Autowired
    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public List<Medication> getAllMedications() {
        return medicationRepository.findAll();
    }

    public Optional<Medication> getMedicationById(Long id) {
        return medicationRepository.findById(id);
    }

    public Medication createMedication(Medication medication) {
        return medicationRepository.save(medication);
    }

    public Medication updateMedication(Long id, Medication medicationDetails) {
        Optional<Medication> medicationOptional = medicationRepository.findById(id);
        if (medicationOptional.isPresent()) {
            Medication medication = medicationOptional.get();
            medication.setName(medicationDetails.getName());
            medication.setManufacturer(medicationDetails.getManufacturer());
            medication.setPrice(medicationDetails.getPrice());
            return medicationRepository.save(medication);
        } else {
            throw new RuntimeException("Medication not found with id " + id);
        }
    }

    public void deleteMedication(Long id) {
        medicationRepository.deleteById(id);
    }
}

