package soft.musala.drone.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import soft.musala.drone.domain.entity.Medication;

import java.util.Set;

/**
 * @author Pargev A. created on 13.04.2023
 */
public interface MedicationRepository extends JpaRepository<Medication, Long> {

    Set<Medication> findAllByDroneId(long droneId);
}
