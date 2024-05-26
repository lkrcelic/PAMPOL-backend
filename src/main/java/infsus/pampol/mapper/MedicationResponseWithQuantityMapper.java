package infsus.pampol.mapper;

import infsus.pampol.dto.response.MedicationResponseWithQuantity;
import infsus.pampol.entity.PharmacyMedication;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MedicationResponseWithQuantityMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public MedicationResponseWithQuantityMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public MedicationResponseWithQuantity toResponse(PharmacyMedication pharmacyMedication) {
        MedicationResponseWithQuantity response =
            modelMapper.map(pharmacyMedication.getMedication(), MedicationResponseWithQuantity.class);
        response.setQuantity(pharmacyMedication.getQuantity());
        return response;
    }

}
