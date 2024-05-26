package infsus.pampol.dto.command;

import lombok.Data;

@Data
public class DoctorUpdateCommand {

    private String firstName;
    private String lastName;
    private String specialty;

}
