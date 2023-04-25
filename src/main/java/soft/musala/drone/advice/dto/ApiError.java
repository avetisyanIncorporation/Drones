package soft.musala.drone.advice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author Pargev A. created on 25.04.2023
 */
@Getter
@AllArgsConstructor
public class ApiError {

    private Integer status;
    private String message;

    public static ApiError badRequest(String message) {
        return new ApiError(HttpStatus.BAD_REQUEST.value(), message);
    }
}
