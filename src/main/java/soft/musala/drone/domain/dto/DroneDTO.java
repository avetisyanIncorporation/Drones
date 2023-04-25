package soft.musala.drone.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import soft.musala.drone.domain.entity.Drone;
import soft.musala.drone.domain.entity.Medication;
import soft.musala.drone.domain.enumeration.DroneModel;
import soft.musala.drone.domain.enumeration.DroneState;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Pargev A. created on 25.04.2023
 */
@Getter
@Setter
public class DroneDTO {

    private Long id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String serialNumber;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DroneModel model;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DroneState state;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer weightLimit;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer availableWeightLimit;

    private Integer batteryCapacity;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<Medication> medications;

    private DroneDTO() {
    }

    private DroneDTO(Drone drone) {
        this.id = drone.getId();
        this.serialNumber = drone.getSerialNumber();
        this.model = DroneModel.valueOf(drone.getModelId());
        this.state = DroneState.valueOf(drone.getStateId());
        this.weightLimit = drone.getWeightLimit();
        this.batteryCapacity = drone.getBatteryCapacity();
        this.medications = drone.getMedications();
        this.availableWeightLimit = weightLimit -
                medications.stream().mapToInt(Medication::getWeight).sum();
    }

    public static List<DroneDTO> of(List<Drone> drones) {
        return drones.stream().map(DroneDTO::new).collect(Collectors.toList());
    }

    public static DroneDTO of(Drone drone) {
        return new DroneDTO(drone);
    }

    public static DroneDTO of(Long id, int batteryCapacity) {
        var droneDto = new DroneDTO();
        droneDto.setId(id);
        droneDto.setBatteryCapacity(batteryCapacity);
        return droneDto;
    }
}
