package soft.musala.drone.domain.enumeration;

import java.util.stream.Stream;

/**
 * @author Pargev A. created on 13.04.2023
 */
public enum DroneModel {

    LIGHTWEIGHT(1),
    MIDDLEWEIGHT(2),
    CRUISERWEIGHT(3),
    HEAVYWEIGHT(4);

    private final int id;

    DroneModel(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static DroneModel valueOf(int id) {
        return Stream.of(values())
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No DroneModel with id=" + id));
    }
}
