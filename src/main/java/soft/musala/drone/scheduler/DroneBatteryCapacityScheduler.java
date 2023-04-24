package soft.musala.drone.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import soft.musala.drone.DroneApplication;
import soft.musala.drone.domain.entity.Drone;
import soft.musala.drone.service.DroneService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Scheduler for checking and logging drones battery capacity.
 *
 * @author Pargev A. created on 16.04.2023
 */
@Component
public class DroneBatteryCapacityScheduler {

    private final Logger log = LoggerFactory.getLogger(DroneApplication.class);

    private final String BATTERY_CAPACITY_LESS_25_PERCENT = "Battery Capacity: 0-25%";
    private final String BATTERY_CAPACITY_LESS_50_PERCENT = "Battery Capacity: 25-50%";
    private final String BATTERY_CAPACITY_LESS_75_PERCENT = "Battery Capacity: 50-75%";
    private final String BATTERY_CAPACITY_LESS_100_PERCENT = "Battery Capacity: 75-100%";

    private final DroneService droneService;

    public DroneBatteryCapacityScheduler(DroneService droneService) {
        this.droneService = droneService;
    }

    @Scheduled(initialDelayString = "${drones.battery.capacity.scheduler.initial.delay.minutes}",
            fixedRateString = "${drones.battery.capacity.scheduler.fixed.rate.minutes}",
            timeUnit = TimeUnit.MINUTES)
    public void reportDronesBatteryCapacity() {
        var dronesMap = initDronesMap();
        completeDronesMapByPercentageGroup(dronesMap);
        auditDrones(dronesMap);
    }

    private Map<String, Set<Drone>> initDronesMap() {
        Map<String, Set<Drone>> dronesMap = new HashMap<>();
        dronesMap.put(BATTERY_CAPACITY_LESS_25_PERCENT, new HashSet<>());
        dronesMap.put(BATTERY_CAPACITY_LESS_50_PERCENT, new HashSet<>());
        dronesMap.put(BATTERY_CAPACITY_LESS_75_PERCENT, new HashSet<>());
        dronesMap.put(BATTERY_CAPACITY_LESS_100_PERCENT, new HashSet<>());
        return dronesMap;
    }

    private void completeDronesMapByPercentageGroup(Map<String, Set<Drone>> dronesMap) {
        for (var drone : droneService.getAllDrones()) {
            var capacity = drone.getBatteryCapacity();
            if (capacity <= 25) {
                dronesMap.get(BATTERY_CAPACITY_LESS_25_PERCENT).add(drone);
            } else if (capacity <= 50) {
                dronesMap.get(BATTERY_CAPACITY_LESS_50_PERCENT).add(drone);
            } else if (capacity <= 75) {
                dronesMap.get(BATTERY_CAPACITY_LESS_75_PERCENT).add(drone);
            } else if (capacity <= 100) {
                dronesMap.get(BATTERY_CAPACITY_LESS_100_PERCENT).add(drone);
            }
        }
    }

    private void auditDrones(Map<String, Set<Drone>> dronesMap) {
        for (var droneSet : dronesMap.entrySet()) {
            var capacityPercentage = droneSet.getKey();
            log.info(capacityPercentage);
            for (var droneInPercentage : droneSet.getValue()) {
                log.info(droneInPercentage.toString());
            }
        }
    }
}
