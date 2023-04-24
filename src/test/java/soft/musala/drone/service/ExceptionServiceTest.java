package soft.musala.drone.service;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import soft.musala.drone.exception.BusinessException;
import soft.musala.drone.utils.TestUtils;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Pargev A. created on 15.04.2023
 */
@ExtendWith(MockitoExtension.class)
class ExceptionServiceTest {

    @InjectMocks
    private ExceptionService exceptionService;

    @Test
    void businessExceptionShouldBeThrownByFields() {
        var serialNumberFieldError = mock(FieldError.class);
        when(serialNumberFieldError.getField()).thenReturn("serialNumber");
        when(serialNumberFieldError.getDefaultMessage()).thenReturn("length might be <= 100");
        var batteryCapacityFieldError = mock(FieldError.class);
        when(batteryCapacityFieldError.getField()).thenReturn("batteryCapacity");
        when(batteryCapacityFieldError.getDefaultMessage()).thenReturn("value might be <= 100");
        var nonFieldError = mock(ObjectError.class);

        var bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(
                List.of(serialNumberFieldError, batteryCapacityFieldError, nonFieldError));

        var exceptionMessage = serialNumberFieldError.getField() + " " + serialNumberFieldError.getDefaultMessage() +
                "\n" +
                batteryCapacityFieldError.getField() + " " + batteryCapacityFieldError.getDefaultMessage();
        TestUtils.checkException(
                () -> exceptionService.throwBusinessExceptionByFieldsError(bindingResult),
                new BusinessException(exceptionMessage)
        );
    }

    @Test
    void resultBodyShouldBeEmptyWhenThrownByEmptyFields() {
        var bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(Collections.emptyList());

        TestUtils.checkException(
                () -> exceptionService.throwBusinessExceptionByFieldsError(bindingResult),
                new BusinessException(Strings.EMPTY)
        );
    }
}
