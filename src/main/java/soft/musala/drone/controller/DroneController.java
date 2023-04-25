package soft.musala.drone.controller;

import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soft.musala.drone.domain.dto.CreateDroneDTO;
import soft.musala.drone.domain.dto.DroneDTO;
import soft.musala.drone.service.DroneService;
import soft.musala.drone.service.ExceptionService;
import soft.musala.drone.service.MedicationService;

import java.util.List;

/**
 * Drone dispatcher controller.
 *
 * @author Pargev A. created on 13.04.2023
 */
@RestController
@RequestMapping(value = "drones")
public class DroneController {

    private final DroneService droneService;
    private final MedicationService medicationService;
    private final ExceptionService exceptionService;

    public DroneController(DroneService droneService,
                           MedicationService medicationService,
                           ExceptionService exceptionService) {
        this.droneService = droneService;
        this.medicationService = medicationService;
        this.exceptionService = exceptionService;
    }

    @GetMapping
    public List<DroneDTO> getAvailable() {
        return DroneDTO.of(droneService.getAllAvailable());
    }

    @PostMapping
    public DroneDTO registerNewDrone(@Valid @ModelAttribute CreateDroneDTO createDroneDTO, BindingResult result) {
        if (result.hasErrors()) {
            exceptionService.throwBusinessExceptionByFieldsError(result);
        }
        return DroneDTO.of(droneService.addNewDrone(createDroneDTO));
    }

    @GetMapping(value = "/{drone-id}/battery-capacity")
    public DroneDTO getDroneBatteryCapacity(@PathVariable("drone-id") Long droneId) {
        return DroneDTO.of(droneId, droneService.getDroneBatteryCapacity(droneId));
    }

    @GetMapping(value = "/{drone-id}/medications")
    public DroneDTO getDroneMedications(@PathVariable("drone-id") Long droneId) {
        return DroneDTO.of(droneService.getDroneById(droneId));
    }

    @Transactional
    @PostMapping(value = "/{drone-id}/medications/{medication-id}")
    public DroneDTO addMedicationToDrone(@PathVariable("drone-id") Long droneId,
                                      @PathVariable("medication-id") Long medicationId) {
        var drone = droneService.getDroneById(droneId);
        var medication = medicationService.getMedication(medicationId);
        drone.getMedications().add(medication);
        medication.setDrone(drone);
        return DroneDTO.of(drone);
    }
}
