package soft.musala.drone.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soft.musala.drone.builder.DroneDTOBuilder;
import soft.musala.drone.domain.entity.Drone;
import soft.musala.drone.domain.entity.Medication;
import soft.musala.drone.domain.enumeration.DroneState;
import soft.musala.drone.domain.repository.DroneRepository;
import soft.musala.drone.utils.TestUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static soft.musala.drone.domain.constant.Constants.AVAILABLE_DRONES_BATTERY_CAPACITY_PERCENTAGE;

/**
 * @author Pargev A. created on 15.04.2023
 */
@ExtendWith(MockitoExtension.class)
class DroneServiceTest {

    @Mock
    private DroneRepository droneRepository;

    @InjectMocks
    private DroneService droneService;

    @Test
    void allDronesShouldBeReturned() {
        droneService.getAllDrones();
        verify(droneRepository, times(1)).findAll();
    }

    @Test
    void allAvailableDronesShouldBeReturned() {
        droneService.getAllAvailable();
        verify(droneRepository, times(1)).findAllByStateIdInAndBatteryCapacityGreaterThan(
                eq(List.of(DroneState.IDLE.getId(), DroneState.LOADING.getId())),
                eq(AVAILABLE_DRONES_BATTERY_CAPACITY_PERCENTAGE)
        );
    }

    @Test
    void droneShouldBeCreated() {
        var droneDTO = new DroneDTOBuilder()
                .serialNumber("aabb1122")
                .modelId(2)
                .stateId(2)
                .weightLimit(400)
                .batteryCapacity(99)
                .build();
        droneService.addNewDrone(droneDTO);

        var argumentCaptor = ArgumentCaptor.forClass(Drone.class);
        verify(droneRepository).save(argumentCaptor.capture());

        var result = argumentCaptor.getValue();
        assertEquals(droneDTO.getSerialNumber(), result.getSerialNumber());
        assertEquals(droneDTO.getModelId(), result.getModelId());
        assertEquals(droneDTO.getStateId(), result.getStateId());
        assertEquals(droneDTO.getWeightLimit(), result.getWeightLimit());
        assertEquals(droneDTO.getBatteryCapacity(), result.getBatteryCapacity());
    }

    @Test
    void batteryCapacityShouldBeReturned() {
        long droneId = 1;
        var battery_percentage = 30;
        var drone = mock(Drone.class);

        when(drone.getBatteryCapacity()).thenReturn(battery_percentage);
        when(droneRepository.findById(eq(droneId))).thenReturn(Optional.of(drone));

        var result = droneService.getDroneBatteryCapacity(droneId);
        assertEquals(battery_percentage, result);
    }

    @Test
    void getBatteryCapacityThrowsIllegalArgumentExceptionWhenCantFindDrone() {
        long droneId = 2;
        when(droneRepository.findById(eq(droneId))).thenReturn(Optional.empty());

        TestUtils.checkException(
                () -> droneService.getDroneBatteryCapacity(2),
                new IllegalArgumentException("No drone with id=" + droneId));
    }

    @Test
    void isNotDroneAvailableWhenWrongState() {
        var drone = mock(Drone.class);
        when(drone.getStateId()).thenReturn(DroneState.DELIVERED.getId());
        assertFalse(droneService.isAvailableForLoading(drone, 50));
    }

    @Test
    void isNotDroneAvailableIfBatteryCapacityLessThanConst() {
        var drone = mock(Drone.class);
        when(drone.getStateId()).thenReturn(DroneState.LOADING.getId());
        when(drone.getBatteryCapacity()).thenReturn(AVAILABLE_DRONES_BATTERY_CAPACITY_PERCENTAGE - 5);
        assertFalse(droneService.isAvailableForLoading(drone, 50));
    }

    @Test
    void isNotDroneAvailableIfWeightLimitExceeded() {
        var drone = mock(Drone.class);
        when(drone.getStateId()).thenReturn(DroneState.IDLE.getId());
        when(drone.getBatteryCapacity()).thenReturn(AVAILABLE_DRONES_BATTERY_CAPACITY_PERCENTAGE + 5);
        var medication50 = mock(Medication.class);
        when(medication50.getWeight()).thenReturn(50);
        var medication100 = mock(Medication.class);
        when(medication100.getWeight()).thenReturn(100);
        when(drone.getMedications()).thenReturn(Set.of(medication50, medication100));
        when(drone.getWeightLimit()).thenReturn(100);
        assertFalse(droneService.isAvailableForLoading(drone, 50));
    }

    @Test
    void droneShouldBeAvailable() {
        var drone = mock(Drone.class);
        when(drone.getStateId()).thenReturn(DroneState.LOADING.getId());
        when(drone.getBatteryCapacity()).thenReturn(AVAILABLE_DRONES_BATTERY_CAPACITY_PERCENTAGE + 5);
        when(drone.getWeightLimit()).thenReturn(200);
        assertTrue(droneService.isAvailableForLoading(drone, 50));
    }
}
