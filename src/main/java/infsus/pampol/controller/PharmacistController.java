package infsus.pampol.controller;

import infsus.pampol.dto.command.PharmacistCreateCommand;
import infsus.pampol.dto.command.PharmacistUpdateCommand;
import infsus.pampol.dto.response.PharmacistResponse;
import infsus.pampol.entity.Pharmacist;
import infsus.pampol.mapper.PharmacistMapper;
import infsus.pampol.service.PharmacistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pharmacists")
public class PharmacistController {

    private final PharmacistService pharmacistService;
    private final PharmacistMapper pharmacistMapper;

    @Autowired
    public PharmacistController(PharmacistService pharmacistService, PharmacistMapper pharmacistMapper) {
        this.pharmacistService = pharmacistService;
        this.pharmacistMapper = pharmacistMapper;
    }

    @GetMapping
    public List<PharmacistResponse> getAllPharmacists() {
        return pharmacistService.getAllPharmacists().stream()
            .map(pharmacistMapper::toResponse)
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PharmacistResponse> getPharmacistById(@PathVariable Long id) {
        Optional<Pharmacist> pharmacist = pharmacistService.getPharmacistById(id);
        return pharmacist.map(value -> ResponseEntity.ok(pharmacistMapper.toResponse(value)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public PharmacistResponse createPharmacist(@RequestBody PharmacistCreateCommand command) {
        Pharmacist pharmacist = pharmacistMapper.toEntity(command);
        return pharmacistMapper.toResponse(pharmacistService.createPharmacist(pharmacist));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PharmacistResponse> updatePharmacist(@PathVariable Long id, @RequestBody
    PharmacistUpdateCommand command) {
        try {
            Pharmacist updatedPharmacist = pharmacistService.updatePharmacist(id, pharmacistMapper.toEntity(command, new Pharmacist()));
            return ResponseEntity.ok(pharmacistMapper.toResponse(updatedPharmacist));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePharmacist(@PathVariable Long id) {
        pharmacistService.deletePharmacist(id);
        return ResponseEntity.noContent().build();
    }
}
