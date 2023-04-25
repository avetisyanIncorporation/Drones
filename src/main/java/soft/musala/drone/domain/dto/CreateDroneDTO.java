package soft.musala.drone.domain.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import static soft.musala.drone.domain.constant.Constants.*;

/**
 * @author Pargev A. created on 14.04.2023
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class CreateDroneDTO {

    @NotNull
    @Size(min = DRONE_SERIAL_NUMBER_MIN_LENGTH, max = DRONE_SERIAL_NUMBER_MAX_LENGTH)
    private String serialNumber;

    private int modelId;
    private int stateId;

    @Min(DRONE_MIN_WEIGHT_LIMIT)
    @Max(DRONE_MAX_WEIGHT_LIMIT)
    private int weightLimit;

    @Min(DRONE_MIN_BATTERY_CAPACITY)
    @Max(DRONE_MAX_BATTERY_CAPACITY)
    private int batteryCapacity;
}
