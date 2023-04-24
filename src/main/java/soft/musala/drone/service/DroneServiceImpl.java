package soft.musala.drone.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import soft.musala.drone.domain.dto.DroneDTO;
import soft.musala.drone.domain.entity.Drone;
import soft.musala.drone.domain.entity.Medication;
import soft.musala.drone.domain.repository.DroneRepository;

import java.util.List;

import static soft.musala.drone.domain.constant.Constants.AVAILABLE_DRONES_BATTERY_CAPACITY_PERCENTAGE;
import static soft.musala.drone.domain.constant.Constants.AVAILABLE_DRONE_STATE_IDS;

/**
 * @author Pargev A. created on 13.04.2023
 */
@Service
public class DroneServiceImpl implements DroneService {

    private final DroneRepository droneRepository;

    public DroneServiceImpl(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    @Override
    public List<Drone> getAllDrones() {
        return droneRepository.findAll();
    }

    @Override
    public List<Drone> getAllAvailable() {
        return droneRepository.findAllByStateIdInAndBatteryCapacityGreaterThan(
                AVAILABLE_DRONE_STATE_IDS, AVAILABLE_DRONES_BATTERY_CAPACITY_PERCENTAGE);
    }

    @Override
    public Drone addNewDrone(DroneDTO droneDTO) {
        return droneRepository.save(new Drone(droneDTO));
    }

    @Override
    public int getDroneBatteryCapacity(long droneId) {
        var drone = getDroneById(droneId);
        return drone.getBatteryCapacity();
    }

    @Override
    public Drone getDroneById(long droneId) {
        return droneRepository.findById(droneId)
                .orElseThrow(() -> new IllegalArgumentException("No drone with id=" + droneId));
    }

    @Override
    public boolean isAvailableForLoading(@NotNull Drone drone, int weight) {
        return isReadyToLoading(drone)
                && isCharged(drone)
                && isFreeWeight(drone, weight);
    }

    private boolean isReadyToLoading(@NotNull Drone drone) {
        return AVAILABLE_DRONE_STATE_IDS.contains(drone.getStateId());
    }

    private boolean isCharged(@NotNull Drone drone) {
        return drone.getBatteryCapacity() >= AVAILABLE_DRONES_BATTERY_CAPACITY_PERCENTAGE;
    }

    private boolean isFreeWeight(@NotNull Drone drone, int weight) {
        return drone.getMedications().stream().mapToInt(Medication::getWeight).sum()
                + weight
                <= drone.getWeightLimit();
    }
}
