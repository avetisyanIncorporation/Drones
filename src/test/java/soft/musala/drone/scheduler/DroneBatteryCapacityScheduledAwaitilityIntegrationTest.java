package soft.musala.drone.scheduler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import soft.musala.drone.service.DroneService;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.verify;

/**
 * @author Pargev A. created on 24.04.2023
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(/*properties = {"drones.battery.capacity.scheduler.initial.delay.minutes=0"}*/)
class DroneBatteryCapacityScheduledAwaitilityIntegrationTest {

    @MockBean
    private DroneService droneService;

    @Test
    public void whenWaitOneSecond_thenScheduledIsCalled() {
        await()
                .atMost(1, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        verify(droneService).getAllDrones()
                )
        ;
    }
}