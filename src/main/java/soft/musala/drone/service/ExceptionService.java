package soft.musala.drone.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.BindingResult;

/**
 * @author Pargev A. created on 14.04.2023
 */
public interface ExceptionService {

    /**
     * Throw BE by BindingResult fields error.
     *
     * @param result of controller data validation
     */
    void throwBusinessExceptionByFieldsError(@NotNull BindingResult result);
}
