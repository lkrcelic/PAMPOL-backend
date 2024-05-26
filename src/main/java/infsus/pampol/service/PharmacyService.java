package infsus.pampol.service;

import infsus.pampol.dto.command.PharmacyCreateCommand;
import infsus.pampol.dto.command.PharmacyUpdateCommand;
import infsus.pampol.dto.response.PharmacyResponse;
import infsus.pampol.entity.*;
import infsus.pampol.mapper.PharmacyMapper;
import infsus.pampol.repository.PharmacyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PharmacyService {

    private final PharmacyRepository pharmacyRepository;
    private final PharmacyMapper pharmacyMapper;

    @Autowired
    public PharmacyService(PharmacyRepository pharmacyRepository, PharmacyMapper pharmacyMapper) {
        this.pharmacyRepository = pharmacyRepository;
        this.pharmacyMapper = pharmacyMapper;
    }

    public List<PharmacyResponse> getAllPharmacies() {
        return pharmacyRepository.findAll().stream()
            .map(pharmacyMapper::toResponse)
            .collect(Collectors.toList());
    }

    public Optional<PharmacyResponse> getPharmacyById(Long id) {
        return pharmacyRepository.findById(id)
            .map(pharmacyMapper::toResponse);
    }

    public PharmacyResponse createPharmacy(PharmacyCreateCommand command) {
        Pharmacy pharmacy = pharmacyMapper.toEntity(command);
        return pharmacyMapper.toResponse(pharmacyRepository.save(pharmacy));
    }

    public PharmacyResponse updatePharmacy(Long id, PharmacyUpdateCommand command) {
        return pharmacyRepository.findById(id)
            .map(existingPharmacy -> {
                Pharmacy updatedPharmacy = pharmacyMapper.toEntity(command, existingPharmacy);
                return pharmacyMapper.toResponse(pharmacyRepository.save(updatedPharmacy));
            }).orElseThrow(() -> new RuntimeException("Pharmacy not found with id " + id));
    }

    public void deletePharmacy(Long id) {
        pharmacyRepository.deleteById(id);
    }
}
