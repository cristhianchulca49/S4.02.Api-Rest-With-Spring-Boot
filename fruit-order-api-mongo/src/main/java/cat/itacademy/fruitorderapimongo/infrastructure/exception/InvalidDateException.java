package cat.itacademy.fruitorderapimongo.infrastructure.exception;

public class InvalidDateException extends RuntimeException {
    public InvalidDateException(String message) {
        super(message);
    }
}
