package infsus.pampol.controller;

import infsus.pampol.dto.command.DoctorCreateCommand;
import infsus.pampol.dto.command.DoctorUpdateCommand;
import infsus.pampol.dto.response.DoctorResponse;
import infsus.pampol.entity.Doctor;
import infsus.pampol.mapper.DoctorMapper;
import infsus.pampol.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    @Autowired
    public DoctorController(DoctorService doctorService, DoctorMapper doctorMapper) {
        this.doctorService = doctorService;
        this.doctorMapper = doctorMapper;
    }

    @GetMapping
    public List<DoctorResponse> getAllDoctors() {
        return doctorService.getAllDoctors().stream()
            .map(doctorMapper::toResponse)
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable Long id) {
        Optional<Doctor> doctor = doctorService.getDoctorById(id);
        return doctor.map(value -> ResponseEntity.ok(doctorMapper.toResponse(value)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public DoctorResponse createDoctor(@RequestBody DoctorCreateCommand command) {
        Doctor doctor = doctorMapper.toEntity(command);
        return doctorMapper.toResponse(doctorService.createDoctor(doctor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> updateDoctor(@PathVariable Long id, @RequestBody DoctorUpdateCommand command) {
        try {
            Doctor updatedDoctor = doctorService.updateDoctor(id, doctorMapper.toEntity(command, new Doctor()));
            return ResponseEntity.ok(doctorMapper.toResponse(updatedDoctor));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }
}