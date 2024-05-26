package infsus.pampol.dto.command;

import lombok.Data;

import java.util.List;

@Data
public class PharmacyUpdateCommand {
    private String name;
    private String address;
    private List<Long> doctorIds;
    private List<Long> pharmacistIds;
    private List<PharmacyMedicationCreateCommand> medications;
}
