package soft.musala.drone.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soft.musala.drone.domain.dto.CreateMedicationDTO;
import soft.musala.drone.domain.entity.Medication;
import soft.musala.drone.domain.enumeration.DroneState;
import soft.musala.drone.domain.repository.MedicationRepository;

import java.util.Objects;

/**
 * Medication management service.
 *
 * @author Pargev A. created on 13.04.2023
 */
@Service
public class MedicationService {

    private final DroneService droneService;
    private final MedicationRepository medicationRepository;

    public MedicationService(DroneService droneService, MedicationRepository medicationRepository) {
        this.droneService = droneService;
        this.medicationRepository = medicationRepository;
    }

    /**
     * Create new Medication and add to drone.
     *
     * @param createMedicationDTO DTO with new Medication info
     * @return created Medication
     * @throws IllegalArgumentException When drone doesn't exist or not available for loading
     */
    @Transactional
    public Medication createMedication(CreateMedicationDTO createMedicationDTO) {
        var medication = new Medication(createMedicationDTO);
        if (Objects.nonNull(createMedicationDTO.getDroneId())) {
            var drone = droneService.getDroneById(createMedicationDTO.getDroneId());
            if (droneService.isAvailableForLoading(drone, createMedicationDTO.getWeight())) {
                drone.setStateId(DroneState.LOADING.getId());
                medication.setDrone(drone);
            } else {
                throw new IllegalArgumentException("Can't loading drone: " + drone);
            }
        }
        return medicationRepository.save(medication);
    }

    /**
     * Get Medication by id from DB.
     *
     * @param medicationId ID
     * @return Medication entity
     * @throws IllegalArgumentException When have no mediation by this id
     */
    public Medication getMedication(long medicationId) {
        return medicationRepository.findById(medicationId)
                .orElseThrow(() -> new IllegalArgumentException("No Medication with id=" + medicationId));
    }

}
