package infsus.pampol.dto.response;

import lombok.Data;

@Data
public class MedicationResponse {

    private Long id;
    private String name;
    private String manufacturer;
    private Double price;

}