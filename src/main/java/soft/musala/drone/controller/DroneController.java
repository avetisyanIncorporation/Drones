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
import soft.musala.drone.domain.dto.DroneDTO;
import soft.musala.drone.domain.entity.Drone;
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
@RequestMapping(value = "drone-management/drones")
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
    public List<Drone> getAvailable() {
        return droneService.getAllAvailable();
    }

    @PostMapping
    public Drone registerNewDrone(@Valid @ModelAttribute DroneDTO droneDTO, BindingResult result) {
        if (result.hasErrors()) {
            exceptionService.throwBusinessExceptionByFieldsError(result);
        }
        return droneService.addNewDrone(droneDTO);
    }

    @GetMapping(value = "/{drone-id}/battery-capacity")
    public Integer getDroneBatteryCapacity(@PathVariable("drone-id") Long droneId) {
        return droneService.getDroneBatteryCapacity(droneId);
    }

    @GetMapping(value = "/{drone-id}/medications")
    public Drone getDroneMedications(@PathVariable("drone-id") Long droneId) {
        return droneService.getDroneById(droneId);
    }

    @Transactional
    @PostMapping(value = "/{drone-id}/medication/{medication-id}/add")
    public Drone addMedicationToDrone(@PathVariable("drone-id") Long droneId,
                                      @PathVariable("medication-id") Long medicationId) {
        var drone = droneService.getDroneById(droneId);
        var medication = medicationService.getMedication(medicationId);
        drone.getMedications().add(medication);
        medication.setDrone(drone);
        return drone;
    }
}
