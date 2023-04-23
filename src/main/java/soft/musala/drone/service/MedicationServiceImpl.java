package soft.musala.drone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soft.musala.drone.domain.dto.MedicationDTO;
import soft.musala.drone.domain.entity.Medication;
import soft.musala.drone.domain.enumeration.DroneState;
import soft.musala.drone.domain.repository.MedicationRepository;

import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author Pargev A. created on 13.04.2023
 */
@Service
public class MedicationServiceImpl implements MedicationService {

    private final DroneService droneService;
    private final MedicationRepository medicationRepository;
    private final Pattern MEDICATION_NAME_PATTERN;
    private final Pattern MEDICATION_CODE_PATTERN;

    @Autowired
    public MedicationServiceImpl(DroneService droneService, MedicationRepository medicationRepository) {
        this.droneService = droneService;
        this.medicationRepository = medicationRepository;
        this.MEDICATION_NAME_PATTERN = Pattern.compile("[a-zA-Z1-9-_]+");
        this.MEDICATION_CODE_PATTERN = Pattern.compile("[A-Z1-9_]+");
    }

    @Override
    @Transactional
    public Set<Medication> getMedicationByDroneId(long droneId) {
        return medicationRepository.findAllByDroneId(droneId);
    }

    @Override
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

    @Override
    public Medication getMedication(long medicationId) {
        return medicationRepository.findById(medicationId)
                .orElseThrow(() -> new IllegalArgumentException("No Medication with id=" + medicationId));
    }

}
