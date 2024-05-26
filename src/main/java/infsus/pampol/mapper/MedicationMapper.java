package infsus.pampol.mapper;

import infsus.pampol.dto.command.MedicationCreateCommand;
import infsus.pampol.dto.command.MedicationUpdateCommand;
import infsus.pampol.dto.response.MedicationResponse;
import infsus.pampol.entity.Medication;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MedicationMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public MedicationMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Medication toEntity(MedicationCreateCommand command) {
        return modelMapper.map(command, Medication.class);
    }

    public Medication toEntity(MedicationUpdateCommand command, Medication medication) {
        modelMapper.map(command, medication);
        return medication;
    }

    public MedicationResponse toResponse(Medication medication) {
        return modelMapper.map(medication, MedicationResponse.class);
    }
}
