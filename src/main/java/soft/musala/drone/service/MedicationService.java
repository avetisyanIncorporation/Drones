package soft.musala.drone.service;

import soft.musala.drone.domain.dto.MedicationDTO;
import soft.musala.drone.domain.entity.Medication;

import java.util.Set;

/**
 * @author Pargev A. created on 13.04.2023
 */
public interface MedicationService {

    /**
     * Find all Medications by drone id.
     *
     * @param droneId drone id
     * @return Medications for drone
     */
    Set<Medication> getMedicationByDroneId(long droneId);

    /**
     * Create new Medication and add to drone.
     *
     * @param medicationDTO DTO with new Medication info
     * @return created Medication
     * @throws IllegalArgumentException When drone doesn't exist or not available for loading
     */
    Medication createMedication(MedicationDTO medicationDTO);

    /**
     * Get Medication by id from DB.
     *
     * @param medicationId ID
     * @return Medication entity
     * @throws IllegalArgumentException When have no mediation by this id
     */
    Medication getMedication(long medicationId);
}
