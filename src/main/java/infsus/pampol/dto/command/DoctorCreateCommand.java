package infsus.pampol.dto.command;

import lombok.Data;

@Data
public class DoctorCreateCommand {
    private String firstName;
    private String lastName;
    private String specialty;
}
