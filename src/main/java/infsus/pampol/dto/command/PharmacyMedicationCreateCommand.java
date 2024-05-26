package infsus.pampol.dto.command;

import lombok.Data;

@Data
public class PharmacyMedicationCreateCommand {

    private Long id;
    private Integer quantity;

}
