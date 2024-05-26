package infsus.pampol.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pharmacy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;

    @OneToMany(mappedBy = "pharmacy", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PharmacyMedication> medications;

    @OneToMany(mappedBy = "pharmacy")
    private Set<Pharmacist> pharmacists;

    @ManyToMany
    @JoinTable(
        name = "Doctor_Pharmacy",
        joinColumns = @JoinColumn(name = "pharmacy_id"),
        inverseJoinColumns = @JoinColumn(name = "doctor_id")
    )
    private Set<Doctor> doctors;

}
