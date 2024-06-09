package infsus.pampol.controller;

import infsus.pampol.dto.command.PharmacyCreateCommand;
import infsus.pampol.dto.command.PharmacyUpdateCommand;
import infsus.pampol.dto.response.PharmacyResponse;
import infsus.pampol.service.PharmacyService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pharmacies")
@CrossOrigin(origins = "http://localhost:3000")
public class PharmacyController {

    private final PharmacyService pharmacyService;
    private final TaskService taskService;

    @Autowired
    public PharmacyController(PharmacyService pharmacyService, TaskService taskService) {
        this.pharmacyService = pharmacyService;
        this.taskService = taskService;
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

    @GetMapping("/tasks/pharmacy")
    public List<Map<String, Object>> getTasks() {
        List<Task> tasks = taskService.createTaskQuery().taskAssignee("pharmacyUser").initializeFormKeys().list();

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

    @PostMapping("/task/pharmacy/confirm/{id}")
    public ResponseEntity<Void> confirmTask(@PathVariable("id") String taskId) {
        taskService.complete(taskId);
        return ResponseEntity.ok().build();
    }
}