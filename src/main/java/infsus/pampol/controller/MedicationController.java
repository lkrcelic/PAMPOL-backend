package infsus.pampol.controller;

import infsus.pampol.dto.command.MedicationCreateCommand;
import infsus.pampol.dto.command.MedicationUpdateCommand;
import infsus.pampol.dto.response.MedicationResponse;
import infsus.pampol.entity.Medication;
import infsus.pampol.mapper.MedicationMapper;
import infsus.pampol.service.MedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medications")
public class MedicationController {

    private final MedicationService medicationService;
    private final MedicationMapper medicationMapper;

    @Autowired
    public MedicationController(MedicationService medicationService, MedicationMapper medicationMapper) {
        this.medicationService = medicationService;
        this.medicationMapper = medicationMapper;
    }

    @GetMapping
    public List<MedicationResponse> getAllMedications() {
        return medicationService.getAllMedications().stream()
            .map(medicationMapper::toResponse)
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicationResponse> getMedicationById(@PathVariable Long id) {
        Optional<Medication> medication = medicationService.getMedicationById(id);
        return medication.map(value -> ResponseEntity.ok(medicationMapper.toResponse(value)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public MedicationResponse createMedication(@RequestBody MedicationCreateCommand command) {
        Medication medication = medicationMapper.toEntity(command);
        return medicationMapper.toResponse(medicationService.createMedication(medication));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicationResponse> updateMedication(@PathVariable Long id, @RequestBody
    MedicationUpdateCommand command) {
        try {
            Medication updatedMedication =
                medicationService.updateMedication(id, medicationMapper.toEntity(command, new Medication()));
            return ResponseEntity.ok(medicationMapper.toResponse(updatedMedication));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long id) {
        medicationService.deleteMedication(id);
        return ResponseEntity.noContent().build();
    }

}