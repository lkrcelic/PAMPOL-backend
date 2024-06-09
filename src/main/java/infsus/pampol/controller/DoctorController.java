package infsus.pampol.controller;

import infsus.pampol.dto.command.DoctorCreateCommand;
import infsus.pampol.dto.command.DoctorUpdateCommand;
import infsus.pampol.dto.response.DoctorResponse;
import infsus.pampol.entity.Doctor;
import infsus.pampol.mapper.DoctorMapper;
import infsus.pampol.service.DoctorService;
import infsus.pampol.service.MedicationService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "http://localhost:3000")
public class DoctorController {

    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final MedicationService medicationService;

    @Autowired
    public DoctorController(DoctorService doctorService, DoctorMapper doctorMapper, RuntimeService runtimeService, TaskService taskService, MedicationService medicationService) {
        this.doctorService = doctorService;
        this.doctorMapper = doctorMapper;
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.medicationService = medicationService;
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

    @GetMapping("/tasks")
    public List<Map<String, Object>> getTasks() {
        List<Task> tasks = taskService.createTaskQuery().taskAssignee("doctorUser").initializeFormKeys().list();

        return tasks.stream()
                .map(task -> {
                    Map<String, Object> details = new HashMap<>();
                    details.put("id", task.getId());
                    details.put("name", task.getName());
                    Map<String, Object> variables = taskService.getVariables(task.getId());
                    details.put("variables", variables);
                    return details;
                })
                .collect(Collectors.toList());
    }

    @PostMapping("/task/confirm/{id}")
    public ResponseEntity<Void> confirmTask(@PathVariable("id") String taskId) {
        taskService.complete(taskId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/start-process")
    public ResponseEntity<Map<String, Object>> startDoctorProcess() {
        boolean medicationExists = !medicationService.getAllMedications().isEmpty();
        Map<String, Object> variables = new HashMap<>();
        variables.put("medicationExists", medicationExists);
        runtimeService.startProcessInstanceByKey("DoctorProcess", variables);
        List<Doctor> doctors = doctorService.getAllDoctors();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Process started successfully");
        response.put("doctors", doctors);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/task/delete-doctor/{id}")
    public ResponseEntity<Void> deleteDoctorTask(@PathVariable("id") String taskId) {
        // Fetch the doctor ID to be deleted from process variables
        Map<String, Object> variables = taskService.getVariables(taskId);
        Long doctorId = (Long) variables.get("doctorId");
        doctorService.deleteDoctor(doctorId);
        taskService.complete(taskId);
        return ResponseEntity.ok().build();
    }
}
