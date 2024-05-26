package infsus.pampol.service;

import infsus.pampol.entity.Pharmacist;
import infsus.pampol.repository.PharmacistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PharmacistService {

    private final PharmacistRepository pharmacistRepository;

    @Autowired
    public PharmacistService(PharmacistRepository pharmacistRepository) {
        this.pharmacistRepository = pharmacistRepository;
    }

    public List<Pharmacist> getAllPharmacists() {
        return pharmacistRepository.findAll();
    }

    public Optional<Pharmacist> getPharmacistById(Long id) {
        return pharmacistRepository.findById(id);
    }

    public Pharmacist createPharmacist(Pharmacist pharmacist) {
        return pharmacistRepository.save(pharmacist);
    }

    public Pharmacist updatePharmacist(Long id, Pharmacist pharmacistDetails) {
        Optional<Pharmacist> pharmacistOptional = pharmacistRepository.findById(id);
        if (pharmacistOptional.isPresent()) {
            Pharmacist pharmacist = pharmacistOptional.get();
            pharmacist.setFirstName(pharmacistDetails.getFirstName());
            pharmacist.setLastName(pharmacistDetails.getLastName());
            pharmacist.setPharmacy(pharmacistDetails.getPharmacy());
            return pharmacistRepository.save(pharmacist);
        } else {
            throw new RuntimeException("Pharmacist not found with id " + id);
        }
    }

    public void deletePharmacist(Long id) {
        pharmacistRepository.deleteById(id);
    }

}