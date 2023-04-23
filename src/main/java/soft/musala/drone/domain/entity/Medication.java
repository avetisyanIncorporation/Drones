package soft.musala.drone.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import soft.musala.drone.domain.dto.MedicationDTO;

import java.util.Objects;

/**
 * @author Pargev A. created on 13.04.2023
 */
@Getter
@Setter
@Entity
@Table(name = "medication")
public class Medication {

    @Id
    @NotNull
    @SequenceGenerator(name = "seq_medication_id_seq", sequenceName = "medication_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_medication_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "weight", nullable = false)
    private int weight;

    @NotNull
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "image")
    private byte[] image;

    @Nullable
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drone_id")
    private Drone drone;

    public Medication() {
    }

    public Medication(MedicationDTO medicationDTO) {
        this.name = medicationDTO.getName();
        this.weight = medicationDTO.getWeight();
        this.code = medicationDTO.getCode();
        this.image = medicationDTO.getImage();
        //
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medication that = (Medication) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Medication{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", weight=" + weight +
                ", code=" + code +
                '}';
    }
}
