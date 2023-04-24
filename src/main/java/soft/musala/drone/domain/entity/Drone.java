package soft.musala.drone.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import soft.musala.drone.domain.dto.DroneDTO;
import soft.musala.drone.domain.enumeration.DroneModel;
import soft.musala.drone.domain.enumeration.DroneState;

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

    @Basic
    @JsonIgnore
    @Column(name = "model_id", nullable = false)
    private int modelId;

    @Transient
    private DroneModel model;

    @Basic
    @JsonIgnore
    @Column(name = "state_id", nullable = false)
    private int stateId;

    @Transient
    private DroneState state;

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

    public Drone(DroneDTO droneDTO) {
        this.serialNumber = droneDTO.getSerialNumber();
        this.modelId = droneDTO.getModelId();
        this.stateId = droneDTO.getStateId();
        this.weightLimit = droneDTO.getWeightLimit();
        this.batteryCapacity = droneDTO.getBatteryCapacity();
    }

    public Set<Medication> getMedications() {
        if (medications == null) {
            medications = new HashSet<>();
        }
        return medications;
    }

    @PostLoad
    void fillTransients() {
        if (modelId > 0) {
            this.model = DroneModel.valueOf(modelId);
        }
        if (stateId > 0) {
            this.state = DroneState.valueOf(stateId);
        }
    }

    @PrePersist
    void fillPersistence() {
        if (model != null) {
            this.modelId = model.getId();
        } else if (modelId > 0) {
            this.model = DroneModel.valueOf(modelId);
        }
        if (state != null) {
            this.stateId = state.getId();
        } else if (stateId > 0) {
            this.state = DroneState.valueOf(stateId);
        }
    }

    @Override
    public String toString() {
        return "Drone{" +
                "id=" + id +
                ", serialNumber='" + serialNumber + '\'' +
                ", model=" + DroneModel.valueOf(modelId) +
                ", state=" + DroneState.valueOf(stateId) +
                ", weightLimit=" + weightLimit +
                ", battery=" + batteryCapacity + "%" +
                '}';
    }
}
