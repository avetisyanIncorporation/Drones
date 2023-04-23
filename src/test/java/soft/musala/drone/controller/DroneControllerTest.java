package soft.musala.drone.controller;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import soft.musala.drone.builder.DroneDTOBuilder;
import soft.musala.drone.domain.entity.Drone;
import soft.musala.drone.domain.enumeration.DroneModel;
import soft.musala.drone.domain.enumeration.DroneState;
import soft.musala.drone.exception.BusinessException;
import soft.musala.drone.service.DroneService;
import soft.musala.drone.service.ExceptionService;
import soft.musala.drone.service.MedicationService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Pargev A. created on 15.04.2023
 */
@WebMvcTest(DroneController.class)
public class DroneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DroneService droneService;

    @MockBean
    private MedicationService medicationService;

    @MockBean
    private ExceptionService exceptionService;

    @Test
    public void getAvailableDronesEmptyTest() throws Exception {
        when(droneService.getAllAvailable()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/drone/getAvailable"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void getAvailableDronesTest() throws Exception {
        // Preparing data
        var drone1Dto = new DroneDTOBuilder()
                .id(1L)
                .serialNumber("1L")
                .modelId(DroneModel.HEAVYWEIGHT.getId())
                .stateId(DroneState.IDLE.getId())
                .weightLimit(500)
                .batteryCapacity(100)
                .build();
        var drone1 = new Drone(drone1Dto);
        var drone2Dto = new DroneDTOBuilder()
                .id(2L)
                .serialNumber("2L")
                .modelId(DroneModel.MIDDLEWEIGHT.getId())
                .stateId(DroneState.LOADING.getId())
                .weightLimit(450)
                .batteryCapacity(80)
                .build();
        var drone2 = new Drone(drone2Dto);

        when(droneService.getAllAvailable()).thenReturn(List.of(drone1, drone2));
        mockMvc.perform(get("/drone/getAvailable"))
                .andDo(print())
                .andExpect(status().isOk())
//                .andExpect(content().string(
//                        containsString("500") || containsString("")
////                                List.of(
////                                        "\"serialNumber\":\"1L\",\"weightLimit\":500,\"batteryCapacity\":100",
////                                        "\"serialNumber\":\"2L\",\"weightLimit\":450,\"batteryCapacity\":80")
//                ))
                ;
    }

    @Test
    public void getDroneBatteryFailureTest() throws Exception {
        var droneId = 1L;
        var exceptionText = "No drone with id=" + droneId;
        when(droneService.getDroneBatteryCapacity(droneId))
                .thenThrow(new IllegalArgumentException(exceptionText));

        mockMvc.perform(get("/drone/" + droneId + "/getBatteryCapacity"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(exceptionText));
    }

    @Test
    public void getDroneBatterySuccessTest() throws Exception {
        var droneId = 1L;
        var batteryCapacity = 99;
        when(droneService.getDroneBatteryCapacity(droneId))
                .thenReturn(batteryCapacity);

        mockMvc.perform(get("/drone/" + droneId + "/getBatteryCapacity"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(batteryCapacity)));
    }

    @Test
    public void registerNewDroneFailureTest() throws Exception {
        var mockExceptionText = "exception in BindingResult";
        doThrow(new BusinessException(mockExceptionText)).when(exceptionService).throwBusinessExceptionByFieldsError(any());

        mockMvc.perform(post("/drone/register").param("serialNumber", Strings.EMPTY))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(mockExceptionText));
    }

    @Test
    public void registerNewDroneSuccessTest() throws Exception {
        var droneDto = new DroneDTOBuilder()
                .serialNumber("1L1L2L2L")
                .modelId(DroneModel.HEAVYWEIGHT.getId())
                .stateId(DroneState.IDLE.getId())
                .weightLimit(500)
                .batteryCapacity(99)
                .build();
        when(droneService.addNewDrone(eq(droneDto))).thenReturn(new Drone(droneDto));

        mockMvc.perform(post("/drone/register")
                        .param("serialNumber", droneDto.getSerialNumber())
                        .param("modelId", String.valueOf(droneDto.getModelId()))
                        .param("stateId", String.valueOf(droneDto.getStateId()))
                        .param("weightLimit", String.valueOf(droneDto.getWeightLimit()))
                        .param("batteryCapacity", String.valueOf(droneDto.getBatteryCapacity()))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":null,\"serialNumber\":\"1L1L2L2L\",\"model\":null,\"state\":null,\"weightLimit\":500,\"batteryCapacity\":99,\"medications\":null}"));
    }
}
