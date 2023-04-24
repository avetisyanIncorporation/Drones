package soft.musala.drone.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soft.musala.drone.domain.dto.MedicationDTO;
import soft.musala.drone.domain.entity.Drone;
import soft.musala.drone.domain.entity.Medication;
import soft.musala.drone.domain.enumeration.DroneState;
import soft.musala.drone.domain.repository.MedicationRepository;
import soft.musala.drone.utils.TestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Pargev A. created on 19.04.2023
 */
@ExtendWith(MockitoExtension.class)
class MedicationServiceTest {

    @Mock
    private DroneService droneService;

    @Mock
    private MedicationRepository medicationRepository;

    @InjectMocks
    private MedicationService medicationService;

    @Test
    void medicationsShouldBeReturnedByDroneId() {
        var droneId = 1L;
        medicationService.getMedicationByDroneId(droneId);
        verify(medicationRepository).findAllByDroneId(eq(droneId));
    }

    @Test
    void medicationShouldBeCreatedWithoutDrone() {
        var medicationDto = new MedicationDTO("Aspirin", 50, "AS-50", new byte[] {22,4,6}, null);
        var medicationCaptor = ArgumentCaptor.forClass(Medication.class);
        medicationService.createMedication(medicationDto);
        verify(medicationRepository).save(medicationCaptor.capture());

        var result = medicationCaptor.getValue();
        assertEquals(medicationDto.getName(), result.getName());
        assertEquals(medicationDto.getWeight(), result.getWeight());
        assertEquals(medicationDto.getCode(), result.getCode());
        assertEquals(medicationDto.getImage(), result.getImage());
    }

    @Test
    void createMedicationThrowIllegalArgumentExceptionIfCantLoadDrone() {
        var droneId = 2L;
        var medicationDto = new MedicationDTO("Aspirin", 50, "AS-50", new byte[] {22,4,6}, droneId);
        var drone = mock(Drone.class);
        when(droneService.getDroneById(droneId)).thenReturn(drone);
        when(droneService.isAvailableForLoading(eq(drone), eq(medicationDto.getWeight()))).thenReturn(false);
        TestUtils.checkException(
                () -> medicationService.createMedication(medicationDto),
                new IllegalArgumentException("Can't loading drone: " + drone));
    }

    @Test
    void medicationShouldBeCreatedWithDrone() {
        var droneId = 2L;
        var medicationDto = new MedicationDTO("Aspirin", 50, "AS-50", new byte[] {22,4,6}, droneId);
        var drone = mock(Drone.class);
        when(droneService.getDroneById(droneId)).thenReturn(drone);
        when(droneService.isAvailableForLoading(eq(drone), eq(medicationDto.getWeight()))).thenReturn(true);
        var medicationCaptor = ArgumentCaptor.forClass(Medication.class);
        medicationService.createMedication(medicationDto);
        verify(medicationRepository).save(medicationCaptor.capture());
        verify(drone).setStateId(DroneState.LOADING.getId());

        var result = medicationCaptor.getValue();
        assertEquals(medicationDto.getName(), result.getName());
        assertEquals(medicationDto.getWeight(), result.getWeight());
        assertEquals(medicationDto.getCode(), result.getCode());
        assertEquals(medicationDto.getImage(), result.getImage());
        assertEquals(drone, result.getDrone());
    }

    @Test
    void getMedicationThrowsIllegalArgumentExceptionIfCantFindMedicationInDb() {
        var medicationId = 3L;
        when(medicationRepository.findById(medicationId)).thenReturn(Optional.empty());
        TestUtils.checkException(
                () -> medicationService.getMedication(medicationId),
                new IllegalArgumentException("No Medication with id=" + medicationId)
        );

    }

    @Test
    void medicationShouldBeReturned() {
        var medicationId = 3L;
        var medication = mock(Medication.class);
        when(medicationRepository.findById(medicationId)).thenReturn(Optional.of(medication));
        var result = medicationService.getMedication(medicationId);
        assertEquals(medication, result);
    }
}
