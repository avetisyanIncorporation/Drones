package soft.musala.drone.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import soft.musala.drone.exception.BusinessException;

import java.util.stream.Collectors;

/**
 * @author Pargev A. created on 14.04.2023
 */
@Service
public class ExceptionServiceImpl implements ExceptionService {

    @Override
    public void throwBusinessExceptionByFieldsError(@NotNull BindingResult result) {
        var errorsResult = result.getAllErrors().stream()
                .filter(err -> err instanceof FieldError)
                .map(fe -> ((FieldError) fe).getField() + " " + fe.getDefaultMessage())
                .collect(Collectors.joining("\n"));
        throw new BusinessException(errorsResult);
    }

}
