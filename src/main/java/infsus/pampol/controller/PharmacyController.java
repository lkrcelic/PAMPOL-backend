package infsus.pampol.controller;

import infsus.pampol.dto.command.PharmacyCreateCommand;
import infsus.pampol.dto.command.PharmacyUpdateCommand;
import infsus.pampol.dto.response.PharmacyResponse;
import infsus.pampol.service.PharmacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacies")
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @Autowired
    public PharmacyController(PharmacyService pharmacyService) {
        this.pharmacyService = pharmacyService;
    }

    @GetMapping
    public List<PharmacyResponse> getAllPharmacies() {
        return pharmacyService.getAllPharmacies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PharmacyResponse> getPharmacyById(@PathVariable Long id) {
        return pharmacyService.getPharmacyById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public PharmacyResponse createPharmacy(@RequestBody PharmacyCreateCommand command) {
        return pharmacyService.createPharmacy(command);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PharmacyResponse> updatePharmacy(@PathVariable Long id,
        @RequestBody PharmacyUpdateCommand command) {
        try {
            return ResponseEntity.ok(pharmacyService.updatePharmacy(id, command));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePharmacy(@PathVariable Long id) {
        pharmacyService.deletePharmacy(id);
        return ResponseEntity.noContent().build();
    }

}
