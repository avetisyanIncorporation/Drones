package soft.musala.drone.domain.enumeration;

import java.util.stream.Stream;

/**
 * @author Pargev A. created on 13.04.2023
 */
public enum DroneState {

    IDLE(1),
    LOADING(2),
    LOADED(3),
    DELIVERING(4),
    DELIVERED(5),
    RETURNING(6);

    private final int id;

    DroneState(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static DroneState valueOf(int id) {
        return Stream.of(values())
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No DroneState with id=" + id));
    }
}
