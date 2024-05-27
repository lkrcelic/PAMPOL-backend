package infsus.pampol.dto.command;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PharmacyMedicationCreateCommand {

    private Long id;
    private Integer quantity;

}
