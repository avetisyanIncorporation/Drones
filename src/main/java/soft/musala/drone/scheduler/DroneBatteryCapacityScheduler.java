package soft.musala.drone.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final int BATTERY_CAPACITY_SCHEDULER_INITIAL_DELAY_IN_MINUTE = 1;
    private final String BATTERY_CAPACITY_LESS_25_PERCENT = "Battery Capacity: 0-25%";
    private final String BATTERY_CAPACITY_GREATER_25_LESS_50_PERCENT = "Battery Capacity: 25-50%";
    private final String BATTERY_CAPACITY_GREATER_50_LESS_75_PERCENT = "Battery Capacity: 50-75%";
    private final String BATTERY_CAPACITY_GREATER_75_PERCENT = "Battery Capacity: 75-100%";

    private final DroneService droneService;

    @Autowired
    public DroneBatteryCapacityScheduler(DroneService droneService) {
        this.droneService = droneService;
    }

    @Scheduled(initialDelay = BATTERY_CAPACITY_SCHEDULER_INITIAL_DELAY_IN_MINUTE,
            fixedRateString = "${drones.battery.capacity.scheduler.in.minutes}",
            timeUnit = TimeUnit.MINUTES)
    public void reportDronesBatteryCapacity() {
        var dronesMap = initDronesMap();
        completeDronesMapByPercentageGroup(dronesMap);
        auditDrones(dronesMap);
    }

    private Map<String, Set<Drone>> initDronesMap() {
        Map<String, Set<Drone>> dronesMap = new HashMap<>();
        dronesMap.put(BATTERY_CAPACITY_LESS_25_PERCENT, new HashSet<>());
        dronesMap.put(BATTERY_CAPACITY_GREATER_25_LESS_50_PERCENT, new HashSet<>());
        dronesMap.put(BATTERY_CAPACITY_GREATER_50_LESS_75_PERCENT, new HashSet<>());
        dronesMap.put(BATTERY_CAPACITY_GREATER_75_PERCENT, new HashSet<>());
        return dronesMap;
    }

    private void completeDronesMapByPercentageGroup(Map<String, Set<Drone>> dronesMap) {
        for (var drone : droneService.getAllDrones()) {
            switch (drone.getBatteryCapacity()) {
                case 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25 ->
                        dronesMap.get(BATTERY_CAPACITY_LESS_25_PERCENT).add(drone);
                case 26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50 ->
                        dronesMap.get(BATTERY_CAPACITY_GREATER_25_LESS_50_PERCENT).add(drone);
                case 51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75 ->
                        dronesMap.get(BATTERY_CAPACITY_GREATER_50_LESS_75_PERCENT).add(drone);
                case 76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100 ->
                        dronesMap.get(BATTERY_CAPACITY_GREATER_75_PERCENT).add(drone);
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
