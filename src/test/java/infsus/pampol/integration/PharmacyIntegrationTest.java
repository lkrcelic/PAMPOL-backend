package infsus.pampol.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import infsus.pampol.dto.command.PharmacyCreateCommand;
import infsus.pampol.dto.command.PharmacyMedicationCreateCommand;
import infsus.pampol.dto.command.PharmacyUpdateCommand;
import infsus.pampol.entity.Doctor;
import infsus.pampol.entity.Medication;
import infsus.pampol.entity.Pharmacist;
import infsus.pampol.entity.Pharmacy;
import infsus.pampol.entity.PharmacyMedication;
import infsus.pampol.repository.DoctorRepository;
import infsus.pampol.repository.MedicationRepository;
import infsus.pampol.repository.PharmacistRepository;
import infsus.pampol.repository.PharmacyMedicationRepository;
import infsus.pampol.repository.PharmacyRepository;
import infsus.pampol.service.PharmacyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PharmacyIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PharmacistRepository pharmacistRepository;

    @Autowired
    private PharmacyMedicationRepository pharmacyMedicationRepository;

    @Autowired
    private MedicationRepository medicationRepository;

    @Autowired
    private PharmacyService pharmacyService;

    @Autowired
    private ModelMapper modelMapper;

    private Long doctorId1;
    private Long doctorId2;
    private Long pharmacistId1;
    private Long pharmacistId2;
    private Long medicationId1;
    private Long medicationId2;
    private Long pharmacyId;

    @BeforeEach
    public void setUp() {
        Pharmacy pharmacy = new Pharmacy();
        pharmacy.setName("Good Health Pharmacy");
        pharmacy.setAddress("789 Pine Street");

        pharmacy = pharmacyRepository.save(pharmacy);
        pharmacyId = pharmacy.getId();

        Doctor doctor1 = new Doctor();
        doctor1.setFirstName("John");
        doctor1.setLastName("Doe");
        doctor1.setSpecialty("Cardiology");
        doctorRepository.save(doctor1);
        doctorId1 = doctor1.getId();

        Doctor doctor2 = new Doctor();
        doctor2.setFirstName("Jane");
        doctor2.setLastName("Smith");
        doctor2.setSpecialty("Dermatology");
        doctorRepository.save(doctor2);
        doctorId2 = doctor2.getId();

        Pharmacist pharmacist1 = new Pharmacist();
        pharmacist1.setFirstName("Clara");
        pharmacist1.setLastName("Oswald");
        pharmacist1.setPharmacy(pharmacy);
        pharmacistRepository.save(pharmacist1);
        pharmacistId1 = pharmacist1.getId();

        Pharmacist pharmacist2 = new Pharmacist();
        pharmacist2.setFirstName("Bruce");
        pharmacist2.setLastName("Wayne");
        pharmacist2.setPharmacy(pharmacy);
        pharmacistRepository.save(pharmacist2);
        pharmacistId2 = pharmacist2.getId();

        Medication medication1 = new Medication();
        medication1.setName("Amoxicillin");
        medication1.setManufacturer("Generic Pharmaceuticals");
        medication1.setPrice(19.99);
        medicationRepository.save(medication1);
        medicationId1 = medication1.getId();

        Medication medication2 = new Medication();
        medication2.setName("Ibuprofen");
        medication2.setManufacturer("Over-The-Counter Meds");
        medication2.setPrice(8.49);
        medicationRepository.save(medication2);
        medicationId2 = medication2.getId();

        PharmacyMedication pharmacyMedication1 = new PharmacyMedication();
        pharmacyMedication1.setPharmacy(pharmacy);
        pharmacyMedication1.setMedication(medication1);
        pharmacyMedication1.setQuantity(100);
        pharmacyMedicationRepository.save(pharmacyMedication1);

        PharmacyMedication pharmacyMedication2 = new PharmacyMedication();
        pharmacyMedication2.setPharmacy(pharmacy);
        pharmacyMedication2.setMedication(medication2);
        pharmacyMedication2.setQuantity(200);
        pharmacyMedicationRepository.save(pharmacyMedication2);

        pharmacy.setDoctors(Set.of(doctor1, doctor2));
        pharmacy.setPharmacists(Set.of(pharmacist1, pharmacist2));
        pharmacy.setMedications(Set.of(pharmacyMedication1, pharmacyMedication2));

        pharmacyRepository.save(pharmacy);
    }

    @Test
    public void testGetPharmacyById() throws Exception {
        Pharmacy pharmacy = pharmacyRepository.findAll().get(0);
        MvcResult result = mockMvc.perform(get("/api/pharmacies/" + pharmacy.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("Good Health Pharmacy");
        assertThat(content).contains("789 Pine Street");

        for (Doctor doctor : pharmacy.getDoctors()) {
            assertThat(content).contains(doctor.getFirstName());
            assertThat(content).contains(doctor.getLastName());
            assertThat(content).contains(doctor.getSpecialty());
        }

        for (Pharmacist pharmacist : pharmacy.getPharmacists()) {
            assertThat(content).contains(pharmacist.getFirstName());
            assertThat(content).contains(pharmacist.getLastName());
        }

        for (var pharmacyMedication : pharmacy.getMedications()) {
            assertThat(content).contains(pharmacyMedication.getMedication().getName());
            assertThat(content).contains(pharmacyMedication.getMedication().getManufacturer());
            assertThat(content).contains(pharmacyMedication.getMedication().getPrice().toString());
            assertThat(content).contains(pharmacyMedication.getQuantity().toString());
        }
    }

    @Test
    public void testCreatePharmacy() throws Exception {
        PharmacyCreateCommand command = new PharmacyCreateCommand();
        command.setName("CarePlus Pharmacy");
        command.setAddress("101 Elm Street");
        command.setDoctorIds(Arrays.asList(doctorId1, doctorId2));
        command.setPharmacistIds(Arrays.asList(pharmacistId1, pharmacistId2));
        PharmacyMedicationCreateCommand medCommand1 = new PharmacyMedicationCreateCommand(medicationId1, 100);
        PharmacyMedicationCreateCommand medCommand2 = new PharmacyMedicationCreateCommand(medicationId2, 200);
        command.setMedications(Arrays.asList(medCommand1, medCommand2));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(command);

        MvcResult result = mockMvc.perform(post("/api/pharmacies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isOk())
            .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("CarePlus Pharmacy");
        assertThat(content).contains("101 Elm Street");

        assertThat(content).contains("John");
        assertThat(content).contains("Doe");
        assertThat(content).contains("Cardiology");
        assertThat(content).contains("Jane");
        assertThat(content).contains("Smith");
        assertThat(content).contains("Dermatology");
        assertThat(content).contains("Clara");
        assertThat(content).contains("Oswald");
        assertThat(content).contains("Bruce");
        assertThat(content).contains("Wayne");
        assertThat(content).contains("Amoxicillin");
        assertThat(content).contains("Generic Pharmaceuticals");
        assertThat(content).contains("19.99");
        assertThat(content).contains("Ibuprofen");
        assertThat(content).contains("Over-The-Counter Meds");
        assertThat(content).contains("8.49");
        assertThat(content).contains("100");
        assertThat(content).contains("200");
    }

    @Test
    public void testUpdatePharmacy() throws Exception {
        PharmacyUpdateCommand command = new PharmacyUpdateCommand();
        command.setName("Updated Pharmacy");
        command.setAddress("123 New Street");
        command.setDoctorIds(Arrays.asList(doctorId1, doctorId2));
        command.setPharmacistIds(Arrays.asList(pharmacistId1, pharmacistId2));
        PharmacyMedicationCreateCommand medCommand1 = new PharmacyMedicationCreateCommand(medicationId1, 150);
        PharmacyMedicationCreateCommand medCommand2 = new PharmacyMedicationCreateCommand(medicationId2, 250);
        command.setMedications(Arrays.asList(medCommand1, medCommand2));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(command);

        MvcResult result = mockMvc.perform(put("/api/pharmacies/" + pharmacyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isOk())
            .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("Updated Pharmacy");
        assertThat(content).contains("123 New Street");

        assertThat(content).contains("John");
        assertThat(content).contains("Doe");
        assertThat(content).contains("Cardiology");
        assertThat(content).contains("Jane");
        assertThat(content).contains("Smith");
        assertThat(content).contains("Dermatology");
        assertThat(content).contains("Clara");
        assertThat(content).contains("Oswald");
        assertThat(content).contains("Bruce");
        assertThat(content).contains("Wayne");
        assertThat(content).contains("Amoxicillin");
        assertThat(content).contains("Generic Pharmaceuticals");
        assertThat(content).contains("19.99");
        assertThat(content).contains("Ibuprofen");
        assertThat(content).contains("Over-The-Counter Meds");
        assertThat(content).contains("8.49");
        assertThat(content).contains("150");
        assertThat(content).contains("250");
    }

}
