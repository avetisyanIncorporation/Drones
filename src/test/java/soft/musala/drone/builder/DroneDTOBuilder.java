package soft.musala.drone.builder;

import soft.musala.drone.domain.dto.DroneDTO;

/**
 * @author Pargev A. created on 15.04.2023
 */
public class DroneDTOBuilder {

    private long id;
    private String serialNumber;
    private int modelId;
    private int stateId;
    private int weightLimit;
    private int batteryCapacity;

    public DroneDTOBuilder(){
    }

    public DroneDTOBuilder id(long id) {
        this.id = id;
        return this;
    }

    public DroneDTOBuilder serialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
        return this;
    }

    public DroneDTOBuilder modelId(int modelId) {
        this.modelId = modelId;
        return this;
    }

    public DroneDTOBuilder stateId(int stateId) {
        this.stateId = stateId;
        return this;
    }

    public DroneDTOBuilder weightLimit(int weightLimit) {
        this.weightLimit = weightLimit;
        return this;
    }

    public DroneDTOBuilder batteryCapacity(int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
        return this;
    }

    public DroneDTO build() {
        return new DroneDTO(serialNumber, modelId, stateId, weightLimit, batteryCapacity);
    }
}
