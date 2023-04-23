package soft.musala.drone.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import soft.musala.drone.exception.BusinessException;

/**
 * Advice for controller exceptions.
 *
 * @author Pargev A. created on 14.04.2023
 */
@ControllerAdvice
public class DefaultControllerAdvice {

    /**
     * Handle controllers businessExceptions and make BAD_REQUEST Response.
     *
     * @param businessException controllers callable method business exceptions
     * @return Response entity with exception message
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(BusinessException businessException) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(businessException.getMessage());
    }

    /**
     * Handle controller methods IllegalArgumentException and make BAD_REQUEST Response.
     *
     * @param illegalArgumentException controllers callable method exceptions
     * @return Response entity with exception message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(illegalArgumentException.getMessage());
    }
}
