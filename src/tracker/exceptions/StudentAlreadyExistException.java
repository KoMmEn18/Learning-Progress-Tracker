package tracker.exceptions;

public class StudentAlreadyExistException extends RuntimeException {
    public StudentAlreadyExistException(String errorMessage) {
        super(errorMessage);
    }
}
