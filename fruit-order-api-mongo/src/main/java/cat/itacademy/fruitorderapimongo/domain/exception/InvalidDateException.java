package cat.itacademy.fruitorderapimongo.domain.exception;

public class InvalidDateException extends RuntimeException {
    public InvalidDateException(String message) {
        super(message);
    }
}
