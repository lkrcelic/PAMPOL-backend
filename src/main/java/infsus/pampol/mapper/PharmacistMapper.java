package infsus.pampol.mapper;

import infsus.pampol.dto.command.PharmacistCreateCommand;
import infsus.pampol.dto.command.PharmacistUpdateCommand;
import infsus.pampol.dto.response.PharmacistResponse;
import infsus.pampol.entity.Pharmacist;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PharmacistMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public PharmacistMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Pharmacist toEntity(PharmacistCreateCommand command) {
        return modelMapper.map(command, Pharmacist.class);
    }

    public Pharmacist toEntity(PharmacistUpdateCommand command, Pharmacist pharmacist) {
        modelMapper.map(command, pharmacist);
        return pharmacist;
    }

    public PharmacistResponse toResponse(Pharmacist pharmacist) {
        return modelMapper.map(pharmacist, PharmacistResponse.class);
    }

}
