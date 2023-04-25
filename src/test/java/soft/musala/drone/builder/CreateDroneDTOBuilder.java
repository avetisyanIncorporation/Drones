package soft.musala.drone.builder;

import soft.musala.drone.domain.dto.CreateDroneDTO;

/**
 * @author Pargev A. created on 15.04.2023
 */
public class CreateDroneDTOBuilder {

    private long id;
    private String serialNumber;
    private int modelId;
    private int stateId;
    private int weightLimit;
    private int batteryCapacity;

    public CreateDroneDTOBuilder(){
    }

    public CreateDroneDTOBuilder id(long id) {
        this.id = id;
        return this;
    }

    public CreateDroneDTOBuilder serialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
        return this;
    }

    public CreateDroneDTOBuilder modelId(int modelId) {
        this.modelId = modelId;
        return this;
    }

    public CreateDroneDTOBuilder stateId(int stateId) {
        this.stateId = stateId;
        return this;
    }

    public CreateDroneDTOBuilder weightLimit(int weightLimit) {
        this.weightLimit = weightLimit;
        return this;
    }

    public CreateDroneDTOBuilder batteryCapacity(int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
        return this;
    }

    public CreateDroneDTO build() {
        return new CreateDroneDTO(serialNumber, modelId, stateId, weightLimit, batteryCapacity);
    }
}
