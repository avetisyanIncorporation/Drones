package soft.musala.drone.domain.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static soft.musala.drone.domain.constant.Constants.MEDICATION_CODE_MAX_LENGTH;
import static soft.musala.drone.domain.constant.Constants.MEDICATION_CODE_MIN_LENGTH;
import static soft.musala.drone.domain.constant.Constants.MEDICATION_CODE_PATTERN;
import static soft.musala.drone.domain.constant.Constants.MEDICATION_MAX_WEIGHT_LIMIT;
import static soft.musala.drone.domain.constant.Constants.MEDICATION_MIN_WEIGHT_LIMIT;
import static soft.musala.drone.domain.constant.Constants.MEDICATION_NAME_PATTERN;

/**
 * @author Pargev A. created on 18.04.2023
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class MedicationDTO {

    @NotNull
    @Pattern(regexp = MEDICATION_NAME_PATTERN)
    private String name;

    @NotNull
    @Min(MEDICATION_MIN_WEIGHT_LIMIT)
    @Max(MEDICATION_MAX_WEIGHT_LIMIT)
    private int weight;

    @NotNull
    @Pattern(regexp = MEDICATION_CODE_PATTERN)
    @Size(min = MEDICATION_CODE_MIN_LENGTH, max = MEDICATION_CODE_MAX_LENGTH)
    private String code;

    @Nullable
    private byte[] image;

    @Nullable
    private Long droneId;
}
