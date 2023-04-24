package soft.musala.drone.advice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import soft.musala.drone.exception.BusinessException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Pargev A. created on 24.04.2023
 */
@ExtendWith(MockitoExtension.class)
class DefaultControllerAdviceTest {

    @InjectMocks
    private DefaultControllerAdvice defaultControllerAdvice;

    @Test
    void businessExceptionShouldBeHandled() {
        String message = "business exception test";
        var be = new BusinessException(message);
        var result = defaultControllerAdvice.handleBusinessException(be);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(message, result.getBody());
    }

    @Test
    void illegalArgumentExceptionShouldBeHandled() {
        String message = "illegal argument test";
        var iae = new IllegalArgumentException(message);
        var result = defaultControllerAdvice.handleIllegalArgumentException(iae);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(message, result.getBody());
    }
}