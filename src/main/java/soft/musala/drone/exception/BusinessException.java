package soft.musala.drone.exception;

/**
 * @author Pargev A. created on 14.04.2023
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
