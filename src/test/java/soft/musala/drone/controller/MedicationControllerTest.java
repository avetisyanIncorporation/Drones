package soft.musala.drone.controller;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import soft.musala.drone.domain.dto.MedicationDTO;
import soft.musala.drone.domain.entity.Medication;
import soft.musala.drone.exception.BusinessException;
import soft.musala.drone.service.ExceptionService;
import soft.musala.drone.service.MedicationService;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Pargev A. created on 19.04.2023
 */
@WebMvcTest(MedicationController.class)
public class MedicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicationService medicationService;

    @MockBean
    private ExceptionService exceptionService;

    @Test
    public void createMedicationFailureTest() throws Exception {
        var mockExceptionText = "exception in BindingResult";
        doThrow(new BusinessException(mockExceptionText)).when(exceptionService).throwBusinessExceptionByFieldsError(any());

        mockMvc.perform(post("/medication/create").param("name", Strings.EMPTY))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(mockExceptionText));
    }

    @Test
    public void createMedicationSuccessTest() throws Exception {
        var medicationDto = new MedicationDTO("Aspirine", 50, "AS50", null, 3L);
        when(medicationService.createMedication(eq(medicationDto))).thenReturn(mock(Medication.class));

        mockMvc.perform(post("/medication/create")
                        .param("name", medicationDto.getName())
                        .param("weight", String.valueOf(medicationDto.getWeight()))
                        .param("code", medicationDto.getCode())
                        .param("image", Arrays.toString(medicationDto.getImage()))
                        .param("droneId", String.valueOf(medicationDto.getDroneId()))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(Strings.EMPTY))
        ;
    }
}
