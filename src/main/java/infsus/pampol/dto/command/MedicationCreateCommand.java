package infsus.pampol.dto.command;

import lombok.Data;

@Data
public class MedicationCreateCommand {

    private String name;
    private String manufacturer;
    private Double price;

}