package soft.musala.drone.scheduler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import soft.musala.drone.service.DroneService;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.verify;

/**
 * @author Pargev A. created on 24.04.2023
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(
//        classes = { ApplicationJpaConfig.class }
//        ,
//        loader = AnnotationConfigContextLoader.class
//)
//@TestPropertySource("classpath:application.properties")
@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"drones.battery.capacity.scheduler.initial.delay.minutes=0"})

class DroneBatteryCapacityScheduledAwaitilityIntegrationTest {

    @Autowired
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