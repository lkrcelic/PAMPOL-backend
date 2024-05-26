package infsus.pampol.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class PharmacyResponse {
    private Long id;
    private String name;
    private String address;
    private List<DoctorResponse> doctors;
    private List<PharmacistResponse> pharmacists;
    private List<MedicationResponseWithQuantity> medications;
}