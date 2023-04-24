package soft.musala.drone.controller;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soft.musala.drone.domain.dto.MedicationDTO;
import soft.musala.drone.domain.entity.Medication;
import soft.musala.drone.service.ExceptionService;
import soft.musala.drone.service.MedicationService;

/**
 * @author Pargev A. created on 14.04.2023
 */
@RestController
@RequestMapping(value = "medication-management/medications")
public class MedicationController {

    private final MedicationService medicationService;
    private final ExceptionService exceptionService;

    public MedicationController(MedicationService medicationService,
                                ExceptionService exceptionService) {
        this.medicationService = medicationService;
        this.exceptionService = exceptionService;
    }

    @PostMapping
    public Medication createMedication(@Valid @ModelAttribute MedicationDTO medicationDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            exceptionService.throwBusinessExceptionByFieldsError(bindingResult);
        }
        return medicationService.createMedication(medicationDTO);
    }
}
