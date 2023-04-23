package soft.musala.drone.domain.constant;

import soft.musala.drone.domain.enumeration.DroneState;

import java.util.List;

/**
 * Application constants.
 *
 * @author Pargev A. created on 15.04.2023
 */
public final class Constants {

    /**
     * Drone serial number length.
     */
    public static final int DRONE_SERIAL_NUMBER_MIN_LENGTH = 1;
    public static final int DRONE_SERIAL_NUMBER_MAX_LENGTH = 100;

    /**
     * Drone weight limit.
     */
    public static final int DRONE_MIN_WEIGHT_LIMIT = 0;
    public static final int DRONE_MAX_WEIGHT_LIMIT = 500;

    /**
     * Drone battery capacity.
     */
    public static final int DRONE_MIN_BATTERY_CAPACITY = 0;
    public static final int DRONE_MAX_BATTERY_CAPACITY = 100;

    /**
     * Percentage of available drones.
     */
    public static final int AVAILABLE_DRONES_BATTERY_CAPACITY_PERCENTAGE = 25;

    /**
     * Available drone states.
     */
    // ToDo Is drones in DroneState.RETURNING available for loading ?
    public static final List<Integer> AVAILABLE_DRONE_STATE_IDS =
            List.of(DroneState.IDLE.getId(), DroneState.LOADING.getId());

    /**
     * Medication code length.
     */
    public static final int MEDICATION_CODE_MIN_LENGTH = 1;
    public static final int MEDICATION_CODE_MAX_LENGTH = 100;

    /**
     * Medication weight limit.
     */
    public static final int MEDICATION_MIN_WEIGHT_LIMIT = 1;
    public static final int MEDICATION_MAX_WEIGHT_LIMIT = DRONE_MAX_WEIGHT_LIMIT;

    /**
     * Medication name and code patterns regexp.
     */
    public static final String MEDICATION_NAME_PATTERN = "[a-zA-Z1-9-_]+";
    public static final String MEDICATION_CODE_PATTERN = "[A-Z1-9_]+";

    private Constants() {
    }
}
