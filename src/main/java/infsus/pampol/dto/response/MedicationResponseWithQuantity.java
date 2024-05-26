package infsus.pampol.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MedicationResponseWithQuantity extends MedicationResponse {
    private Integer quantity;
}
