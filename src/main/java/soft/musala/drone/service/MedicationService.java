package soft.musala.drone.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soft.musala.drone.domain.dto.MedicationDTO;
import soft.musala.drone.domain.entity.Medication;
import soft.musala.drone.domain.enumeration.DroneState;
import soft.musala.drone.domain.repository.MedicationRepository;

import java.util.Objects;
import java.util.Set;

/**
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
     * Find all Medications by drone id.
     *
     * @param droneId drone id
     * @return Medications for drone
     */
    @Transactional
    public Set<Medication> getMedicationByDroneId(long droneId) {
        return medicationRepository.findAllByDroneId(droneId);
    }

    /**
     * Create new Medication and add to drone.
     *
     * @param medicationDTO DTO with new Medication info
     * @return created Medication
     * @throws IllegalArgumentException When drone doesn't exist or not available for loading
     */
    @Transactional
    public Medication createMedication(MedicationDTO medicationDTO) {
        var medication = new Medication(medicationDTO);
        if (Objects.nonNull(medicationDTO.getDroneId())) {
            var drone = droneService.getDroneById(medicationDTO.getDroneId());
            if (droneService.isAvailableForLoading(drone, medicationDTO.getWeight())) {
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
