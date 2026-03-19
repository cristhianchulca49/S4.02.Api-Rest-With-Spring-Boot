package cat.itacademy.fruitorderapimongo.infrastructure.exception;

public class InvalidNameException extends RuntimeException {
    public InvalidNameException(String message) {
        super(message);
    }
}
