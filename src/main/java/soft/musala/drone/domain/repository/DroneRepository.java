package soft.musala.drone.domain.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import soft.musala.drone.domain.entity.Drone;

import java.util.List;

/**
 * @author Pargev A. created on 13.04.2023
 */
public interface DroneRepository extends JpaRepository<Drone, Long> {

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = "medications")
    List<Drone> findAllByStateIdInAndBatteryCapacityGreaterThan(List<Integer> stateIds, int availableBattery);

}
