package soft.musala.drone.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "drone")
public class DroneController {

    private final DroneService droneService;
    private final MedicationService medicationService;
    private final ExceptionService exceptionService;

    @Autowired
    public DroneController(DroneService droneService,
                           MedicationService medicationService,
                           ExceptionService exceptionService) {
        this.droneService = droneService;
        this.medicationService = medicationService;
        this.exceptionService = exceptionService;
    }

    @PostMapping(value = "register")
    public Drone registerNewDrone(@Valid @ModelAttribute DroneDTO droneDTO, BindingResult result) {
        if (result.hasErrors()) {
            exceptionService.throwBusinessExceptionByFieldsError(result);
        }
        return droneService.addNewDrone(droneDTO);
    }

    @GetMapping(value = "getAvailable")
    public List<Drone> getAllAvailable() {
        return droneService.getAllAvailable();
    }

    @GetMapping(value = "/{droneId}/getBatteryCapacity")
    public Integer getDroneBatteryCapacity(@PathVariable Long droneId) {
        return droneService.getDroneBatteryCapacity(droneId);
    }

    @GetMapping(value = "/{droneId}/getMedications")
    public Drone getDroneMedications(@PathVariable Long droneId) {
        return droneService.getDroneById(droneId);
    }

    @Transactional
    @PostMapping(value = "/{droneId}/addMedication/{medicationId}")
    public Drone addMedicationToDrone(@PathVariable Long droneId, @PathVariable Long medicationId) {
        var drone = droneService.getDroneById(droneId);
        var medication = medicationService.getMedication(medicationId);
        drone.getMedications().add(medication);
        medication.setDrone(drone);
        return drone;
    }
}
