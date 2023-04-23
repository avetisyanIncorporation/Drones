package soft.musala.drone.service;

import jakarta.validation.constraints.NotNull;
import soft.musala.drone.domain.dto.DroneDTO;
import soft.musala.drone.domain.entity.Drone;

import java.util.List;

/**
 * @author Pargev A. created on 13.04.2023
 */
public interface DroneService {

    /**
     * Get All Drones.
     *
     * @return All drones
     */
    List<Drone> getAllDrones();

    /**
     * Get all available drones for loading.
     *
     * @return All available drones
     */
    List<Drone> getAllAvailable();

    /**
     * Add (Register) new drone in db.
     *
     * @param droneDTO new drone info
     * @return saved drone from db
     */
    Drone addNewDrone(DroneDTO droneDTO);

    /**
     * Get drone from db and get his battery capacity.
     *
     * @param droneId drone ID
     * @return battery capacity
     */
    int getDroneBatteryCapacity(long droneId);

    /**
     * Get Drone by id.
     *
     * @param droneId drone id
     * @return Drone
     * @throws IllegalArgumentException if not found
     */
    Drone getDroneById(long droneId);

    /**
     * Checking Drone State, battery capacity and free weight to be loaded.
     *
     * @param drone drone for loading
     * @param weight Weight size
     * @return true/false for ability to loading
     */
    boolean isAvailableForLoading(@NotNull Drone drone, int weight);
}
