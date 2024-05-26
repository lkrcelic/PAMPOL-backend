package infsus.pampol.mapper;

import infsus.pampol.dto.command.PharmacyCreateCommand;
import infsus.pampol.dto.command.PharmacyUpdateCommand;
import infsus.pampol.dto.response.PharmacyResponse;
import infsus.pampol.entity.Pharmacy;
import infsus.pampol.entity.PharmacyMedication;
import infsus.pampol.repository.DoctorRepository;
import infsus.pampol.repository.MedicationRepository;
import infsus.pampol.repository.PharmacistRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PharmacyMapper {

    private final ModelMapper modelMapper;
    private final DoctorRepository doctorRepository;
    private final PharmacistRepository pharmacistRepository;
    private final MedicationRepository medicationRepository;
    private final DoctorMapper doctorMapper;
    private final PharmacistMapper pharmacistMapper;
    private final MedicationResponseWithQuantityMapper medicationResponseWithQuantityMapper;

    @Autowired
    public PharmacyMapper(ModelMapper modelMapper, DoctorRepository doctorRepository,
        PharmacistRepository pharmacistRepository, MedicationRepository medicationRepository, DoctorMapper doctorMapper,
        PharmacistMapper pharmacistMapper, MedicationResponseWithQuantityMapper medicationResponseWithQuantityMapper) {
        this.modelMapper = modelMapper;
        this.doctorRepository = doctorRepository;
        this.pharmacistRepository = pharmacistRepository;
        this.medicationRepository = medicationRepository;
        this.doctorMapper = doctorMapper;
        this.pharmacistMapper = pharmacistMapper;
        this.medicationResponseWithQuantityMapper = medicationResponseWithQuantityMapper;
    }

    public Pharmacy toEntity(PharmacyCreateCommand command) {
        Pharmacy pharmacy = modelMapper.map(command, Pharmacy.class);

        pharmacy.setDoctors(command.getDoctorIds().stream()
            .map(doctorRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toSet()));

        pharmacy.setPharmacists(command.getPharmacistIds().stream()
            .map(pharmacistRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toSet()));

        pharmacy.setMedications(command.getMedications().stream()
            .map(medicationCommand -> {
                PharmacyMedication pharmacyMedication = new PharmacyMedication();
                pharmacyMedication.setMedication(medicationRepository.findById(medicationCommand.getId())
                    .orElseThrow(() -> new RuntimeException("Medication not found")));
                pharmacyMedication.setQuantity(medicationCommand.getQuantity());
                pharmacyMedication.setPharmacy(pharmacy);
                return pharmacyMedication;
            }).collect(Collectors.toSet()));

        return pharmacy;
    }

    public Pharmacy toEntity(PharmacyUpdateCommand command, Pharmacy pharmacy) {
        modelMapper.map(command, pharmacy);

        pharmacy.setDoctors(command.getDoctorIds().stream()
            .map(doctorRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toSet()));

        pharmacy.setPharmacists(command.getPharmacistIds().stream()
            .map(pharmacistRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toSet()));

        pharmacy.setMedications(command.getMedications().stream()
            .map(medicationCommand -> {
                PharmacyMedication pharmacyMedication = new PharmacyMedication();
                pharmacyMedication.setMedication(medicationRepository.findById(medicationCommand.getId())
                    .orElseThrow(() -> new RuntimeException("Medication not found")));
                pharmacyMedication.setQuantity(medicationCommand.getQuantity());
                pharmacyMedication.setPharmacy(pharmacy);
                return pharmacyMedication;
            }).collect(Collectors.toSet()));

        return pharmacy;
    }

    public PharmacyResponse toResponse(Pharmacy pharmacy) {
        PharmacyResponse response = modelMapper.map(pharmacy, PharmacyResponse.class);

        // Handle medications mapping
        response.setMedications(pharmacy.getMedications().stream()
            .map(medicationResponseWithQuantityMapper::toResponse)
            .collect(Collectors.toList()));

        // Handle doctors mapping
        response.setDoctors(pharmacy.getDoctors().stream()
            .map(doctorMapper::toResponse)
            .collect(Collectors.toList()));

        // Handle pharmacists mapping
        response.setPharmacists(pharmacy.getPharmacists().stream()
            .map(pharmacistMapper::toResponse)
            .collect(Collectors.toList()));

        return response;
    }

}
