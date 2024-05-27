package infsus.pampol.service;

import infsus.pampol.dto.command.PharmacyCreateCommand;
import infsus.pampol.dto.command.PharmacyUpdateCommand;
import infsus.pampol.dto.response.PharmacyResponse;
import infsus.pampol.entity.Pharmacy;
import infsus.pampol.mapper.PharmacyMapper;
import infsus.pampol.repository.MedicationRepository;
import infsus.pampol.repository.PharmacyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PharmacyServiceTest {

    private PharmacyService pharmacyService;

    private MedicationRepository medicationRepository;

    private PharmacyRepository pharmacyRepository;

    private PharmacyMapper pharmacyMapper;

    private Pharmacy pharmacy;

    private PharmacyResponse pharmacyResponse;

    @BeforeEach
    public void setup() {
        medicationRepository = Mockito.mock(MedicationRepository.class);
        pharmacyRepository = Mockito.mock(PharmacyRepository.class);
        pharmacyMapper = Mockito.mock(PharmacyMapper.class);
        pharmacyService = new PharmacyService(medicationRepository, pharmacyRepository, pharmacyMapper);

        pharmacy = new Pharmacy();
        pharmacy.setId(1L);
        pharmacy.setName("Good Health Pharmacy");
        pharmacy.setAddress("789 Pine Street");

        pharmacyResponse = new PharmacyResponse();
        pharmacyResponse.setId(1L);
        pharmacyResponse.setName("Good Health Pharmacy");
        pharmacyResponse.setAddress("789 Pine Street");
    }

    @Test
    public void testGetAllPharmacies() {
        when(pharmacyRepository.findAll()).thenReturn(Arrays.asList(pharmacy));
        when(pharmacyMapper.toResponse(any(Pharmacy.class))).thenReturn(pharmacyResponse);

        assertThat(pharmacyService.getAllPharmacies()).hasSize(1).contains(pharmacyResponse);
    }

    @Test
    public void testGetPharmacyById() {
        when(pharmacyRepository.findById(1L)).thenReturn(Optional.of(pharmacy));
        when(pharmacyMapper.toResponse(any(Pharmacy.class))).thenReturn(pharmacyResponse);

        Optional<PharmacyResponse> foundPharmacy = pharmacyService.getPharmacyById(1L);
        assertThat(foundPharmacy).isPresent();
        assertThat(foundPharmacy.get().getName()).isEqualTo("Good Health Pharmacy");
    }

    @Test
    public void testCreatePharmacy() {
        PharmacyCreateCommand command = new PharmacyCreateCommand();
        command.setName("Good Health Pharmacy");
        command.setAddress("789 Pine Street");

        when(pharmacyMapper.toEntity(any(PharmacyCreateCommand.class))).thenReturn(pharmacy);
        when(pharmacyRepository.save(any(Pharmacy.class))).thenReturn(pharmacy);
        when(pharmacyMapper.toResponse(any(Pharmacy.class))).thenReturn(pharmacyResponse);

        PharmacyResponse createdPharmacy = pharmacyService.createPharmacy(command);
        assertThat(createdPharmacy.getName()).isEqualTo("Good Health Pharmacy");
    }

    @Test
    public void testUpdatePharmacy() {
        PharmacyUpdateCommand command = new PharmacyUpdateCommand();
        command.setName("Good Health Pharmacy");
        command.setAddress("789 Pine Street");

        when(pharmacyRepository.findById(1L)).thenReturn(Optional.of(pharmacy));
        when(pharmacyMapper.toEntity(any(PharmacyUpdateCommand.class), any(Pharmacy.class))).thenReturn(pharmacy);
        when(pharmacyRepository.save(any(Pharmacy.class))).thenReturn(pharmacy);
        when(pharmacyMapper.toResponse(any(Pharmacy.class))).thenReturn(pharmacyResponse);

        PharmacyResponse updatedPharmacy = pharmacyService.updatePharmacy(1L, command);
        assertThat(updatedPharmacy.getName()).isEqualTo("Good Health Pharmacy");
    }

    @Test
    public void testDeletePharmacy() {
        pharmacyService.deletePharmacy(1L);
        Mockito.verify(pharmacyRepository, Mockito.times(1)).deleteById(1L);
    }

}
