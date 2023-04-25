package soft.musala.drone.domain.dto;

import lombok.Getter;
import soft.musala.drone.domain.entity.Medication;

/**
 * @author Pargev A. created on 25.04.2023
 */
@Getter
public class MedicationDTO {

    private Long id;
    private String name;
    private int weight;
    private String code;
    private byte[] image;

    private MedicationDTO(Medication medication) {
        this.id = medication.getId();
        this.name = medication.getName();
        this.weight = medication.getWeight();
        this.code = medication.getCode();
        this.image = medication.getImage();
    }

    public static MedicationDTO of(Medication medication) {
        return new MedicationDTO(medication);
    }
}
