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
public class DroneService {

    private final DroneRepository droneRepository;

    public DroneService(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    /**
     * Get All Drones.
     *
     * @return All drones
     */
    public List<Drone> getAllDrones() {
        return droneRepository.findAll();
    }

    /**
     * Get all available drones for loading.
     *
     * @return All available drones
     */
    public List<Drone> getAllAvailable() {
        return droneRepository.findAllByStateIdInAndBatteryCapacityGreaterThan(
                AVAILABLE_DRONE_STATE_IDS, AVAILABLE_DRONES_BATTERY_CAPACITY_PERCENTAGE);
    }

    /**
     * Add (Register) new drone in db.
     *
     * @param droneDTO new drone info
     * @return saved drone from db
     */
    public Drone addNewDrone(DroneDTO droneDTO) {
        return droneRepository.save(new Drone(droneDTO));
    }

    /**
     * Get drone from db and get his battery capacity.
     *
     * @param droneId drone ID
     * @return battery capacity
     */
    public int getDroneBatteryCapacity(long droneId) {
        var drone = getDroneById(droneId);
        return drone.getBatteryCapacity();
    }

    /**
     * Get Drone by id.
     *
     * @param droneId drone id
     * @return Drone
     * @throws IllegalArgumentException if not found
     */
    public Drone getDroneById(long droneId) {
        return droneRepository.findById(droneId)
                .orElseThrow(() -> new IllegalArgumentException("No drone with id=" + droneId));
    }

    /**
     * Checking Drone State, battery capacity and free weight to be loaded.
     *
     * @param drone drone for loading
     * @param weight Weight size
     * @return true/false for ability to loading
     */
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
