package soft.musala.drone.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import soft.musala.drone.domain.dto.CreateDroneDTO;

import java.util.HashSet;
import java.util.Set;

import static soft.musala.drone.domain.constant.Constants.DRONE_MAX_BATTERY_CAPACITY;
import static soft.musala.drone.domain.constant.Constants.DRONE_MAX_WEIGHT_LIMIT;
import static soft.musala.drone.domain.constant.Constants.DRONE_MIN_BATTERY_CAPACITY;
import static soft.musala.drone.domain.constant.Constants.DRONE_MIN_WEIGHT_LIMIT;
import static soft.musala.drone.domain.constant.Constants.DRONE_SERIAL_NUMBER_MAX_LENGTH;
import static soft.musala.drone.domain.constant.Constants.DRONE_SERIAL_NUMBER_MIN_LENGTH;

/**
 * @author Pargev A. created on 13.04.2023
 */
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(exclude = "medications")
@Entity
@Table(name = "drone")
public class Drone {

    @Id
    @SequenceGenerator(name = "drone_id_seq", sequenceName = "drone_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "drone_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Size(min = DRONE_SERIAL_NUMBER_MIN_LENGTH, max = DRONE_SERIAL_NUMBER_MAX_LENGTH)
    @Column(name = "serial_number", nullable = false, unique = true)
    private String serialNumber;

    @Column(name = "model_id", nullable = false)
    private int modelId;

    @Column(name = "state_id", nullable = false)
    private int stateId;

    @Min(DRONE_MIN_WEIGHT_LIMIT)
    @Max(DRONE_MAX_WEIGHT_LIMIT)
    @Column(name = "weight_limit", nullable = false)
    private int weightLimit;

    @Min(DRONE_MIN_BATTERY_CAPACITY)
    @Max(DRONE_MAX_BATTERY_CAPACITY)
    @Column(name = "battery_capacity", nullable = false)
    private int batteryCapacity;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "drone")
    private Set<Medication> medications;

    public Drone() {
    }

    public Drone(CreateDroneDTO createDroneDTO) {
        this.serialNumber = createDroneDTO.getSerialNumber();
        this.modelId = createDroneDTO.getModelId();
        this.stateId = createDroneDTO.getStateId();
        this.weightLimit = createDroneDTO.getWeightLimit();
        this.batteryCapacity = createDroneDTO.getBatteryCapacity();
    }

    public Set<Medication> getMedications() {
        if (medications == null) {
            medications = new HashSet<>();
        }
        return medications;
    }
}
