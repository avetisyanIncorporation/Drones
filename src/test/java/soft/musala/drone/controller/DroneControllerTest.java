package soft.musala.drone.controller;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import soft.musala.drone.builder.DroneDTOBuilder;
import soft.musala.drone.domain.dto.MedicationDTO;
import soft.musala.drone.domain.entity.Drone;
import soft.musala.drone.domain.entity.Medication;
import soft.musala.drone.domain.enumeration.DroneModel;
import soft.musala.drone.domain.enumeration.DroneState;
import soft.musala.drone.exception.BusinessException;
import soft.musala.drone.service.DroneService;
import soft.musala.drone.service.ExceptionService;
import soft.musala.drone.service.MedicationService;

import java.util.Collections;
import java.util.List;
import java.util.Set;

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

    private final String DRONES_MANAGEMENT_URL = "/drone-management/drones";
    private final String DRONES_MANAGEMENT_FULL_URL = "/drone-management/drones/";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DroneService droneService;

    @MockBean
    private MedicationService medicationService;

    @MockBean
    private ExceptionService exceptionService;

    @Test
    public void emptyAvailableDrones() throws Exception {
        when(droneService.getAllAvailable()).thenReturn(Collections.emptyList());
        mockMvc.perform(get(DRONES_MANAGEMENT_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void availableDrones() throws Exception {
        var drone1Dto = new DroneDTOBuilder()
                .id(1L)
                .serialNumber("1L")
                .modelId(DroneModel.HEAVYWEIGHT.getId())
                .stateId(DroneState.IDLE.getId())
                .weightLimit(500)
                .batteryCapacity(100)
                .build();
        var drone1 = new Drone(drone1Dto);

        when(droneService.getAllAvailable()).thenReturn(List.of(drone1));
        mockMvc.perform(get(DRONES_MANAGEMENT_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"id\":null,\"serialNumber\":\"1L\",\"model\":null,\"state\":null,\"weightLimit\":500,\"batteryCapacity\":100,\"medications\":[]}]"));
    }

    @Test
    public void failToAddNewDroneWhenBindingResultHasErrors() throws Exception {
        var mockExceptionText = "exception in BindingResult";
        doThrow(new BusinessException(mockExceptionText)).when(exceptionService).throwBusinessExceptionByFieldsError(any());

        mockMvc.perform(post(DRONES_MANAGEMENT_URL).param("serialNumber", Strings.EMPTY))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(mockExceptionText));
    }

    @Test
    public void registerNewDrone() throws Exception {
        var droneDto = new DroneDTOBuilder()
                .serialNumber("1L1L2L2L")
                .modelId(DroneModel.HEAVYWEIGHT.getId())
                .stateId(DroneState.IDLE.getId())
                .weightLimit(500)
                .batteryCapacity(99)
                .build();
        when(droneService.addNewDrone(eq(droneDto))).thenReturn(new Drone(droneDto));

        mockMvc.perform(post(DRONES_MANAGEMENT_URL)
                        .param("serialNumber", droneDto.getSerialNumber())
                        .param("modelId", String.valueOf(droneDto.getModelId()))
                        .param("stateId", String.valueOf(droneDto.getStateId()))
                        .param("weightLimit", String.valueOf(droneDto.getWeightLimit()))
                        .param("batteryCapacity", String.valueOf(droneDto.getBatteryCapacity()))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":null,\"serialNumber\":\"1L1L2L2L\",\"model\":null,\"state\":null,\"weightLimit\":500,\"batteryCapacity\":99,\"medications\":[]}"));
    }

    @Test
    public void failToGetBatteryCapacityWhenHaveNoDrone() throws Exception {
        var droneId = 1L;
        var exceptionText = "No drone with id=" + droneId;
        when(droneService.getDroneBatteryCapacity(droneId))
                .thenThrow(new IllegalArgumentException(exceptionText));

        mockMvc.perform(get(DRONES_MANAGEMENT_FULL_URL + droneId + "/battery-capacity"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(exceptionText));
    }

    @Test
    public void getBatteryCapacity() throws Exception {
        var droneId = 1L;
        var batteryCapacity = 99;
        when(droneService.getDroneBatteryCapacity(droneId))
                .thenReturn(batteryCapacity);

        mockMvc.perform(get(DRONES_MANAGEMENT_FULL_URL + droneId + "/battery-capacity"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(batteryCapacity)));
    }

    @Test
    public void failToGetMedicationsWhenHaveNoDrone() throws Exception {
        var droneId = 1L;
        var exceptionText = "No drone with id=" + droneId;
        when(droneService.getDroneById(droneId))
                .thenThrow(new IllegalArgumentException(exceptionText));

        mockMvc.perform(get(DRONES_MANAGEMENT_FULL_URL + droneId + "/medications"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(exceptionText));
    }

    @Test
    public void getDroneMedications() throws Exception {
        var droneId = 11L;
        var droneDto = new DroneDTOBuilder()
                .serialNumber("1L1L2L2L")
                .modelId(DroneModel.HEAVYWEIGHT.getId())
                .stateId(DroneState.IDLE.getId())
                .weightLimit(500)
                .batteryCapacity(99)
                .build();
        var drone = new Drone(droneDto);
        var medicationDto = new MedicationDTO("Aspirin", 50, "AS_50");
        drone.setMedications(Set.of(new Medication(medicationDto)));

        when(droneService.getDroneById(droneId)).thenReturn(drone);
        mockMvc.perform(get(DRONES_MANAGEMENT_FULL_URL + droneId + "/medications"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":null,\"serialNumber\":\"1L1L2L2L\",\"model\":null,\"state\":null,\"weightLimit\":500,\"batteryCapacity\":99,\"medications\":[{\"id\":null,\"name\":\"Aspirin\",\"weight\":50,\"code\":\"AS_50\",\"image\":null}]}"));
    }

    @Test
    public void failToAddMedicationToDroneIfHaveNoDrone() throws Exception {
        var droneId = 1L;
        var medicationId = 2L;
        var exceptionMessage = "Have no drone";
        when(droneService.getDroneById(eq(droneId)))
                .thenThrow(new IllegalArgumentException(exceptionMessage));

        mockMvc.perform(post(DRONES_MANAGEMENT_FULL_URL + droneId + "/medication/" + medicationId + "/add"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(exceptionMessage));
    }

    @Test
    public void failToAddMedicationToDroneIfHaveNoMedication() throws Exception {
        var droneId = 1L;
        var medicationId = 2L;
        when(droneService.getDroneById(eq(droneId))).thenReturn(new Drone());
        var exceptionMessage = "Have no medication";
        when(medicationService.getMedication(eq(medicationId)))
                .thenThrow(new IllegalArgumentException(exceptionMessage));

        mockMvc.perform(post(DRONES_MANAGEMENT_FULL_URL + droneId + "/medication/" + medicationId + "/add"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(exceptionMessage));
    }

    @Test
    public void addMedicationToDrone() throws Exception {
        var droneId = 1L;
        var drone = new Drone();
        drone.setId(droneId);
        when(droneService.getDroneById(eq(droneId))).thenReturn(drone);

        var medicationId = 2L;
        var medication = new Medication();
        medication.setId(medicationId);
        when(medicationService.getMedication(eq(medicationId))).thenReturn(medication);

        mockMvc.perform(post(DRONES_MANAGEMENT_FULL_URL + droneId + "/medication/" + medicationId + "/add"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":1,\"serialNumber\":null,\"model\":null,\"state\":null,\"weightLimit\":0,\"batteryCapacity\":0,\"medications\":[{\"id\":2,\"name\":null,\"weight\":0,\"code\":null,\"image\":null}]}"));
    }
}
