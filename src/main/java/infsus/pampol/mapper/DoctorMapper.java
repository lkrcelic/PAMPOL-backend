package infsus.pampol.mapper;

import infsus.pampol.dto.command.DoctorCreateCommand;
import infsus.pampol.dto.command.DoctorUpdateCommand;
import infsus.pampol.dto.response.DoctorResponse;
import infsus.pampol.entity.Doctor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public DoctorMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Doctor toEntity(DoctorCreateCommand command) {
        return modelMapper.map(command, Doctor.class);
    }

    public Doctor toEntity(DoctorUpdateCommand command, Doctor doctor) {
        modelMapper.map(command, doctor);
        return doctor;
    }

    public DoctorResponse toResponse(Doctor doctor) {
        return modelMapper.map(doctor, DoctorResponse.class);
    }
}