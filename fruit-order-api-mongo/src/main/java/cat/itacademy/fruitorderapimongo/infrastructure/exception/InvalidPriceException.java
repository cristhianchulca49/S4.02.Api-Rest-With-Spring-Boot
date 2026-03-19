package cat.itacademy.fruitorderapimongo.infrastructure.exception;

public class InvalidPriceException extends RuntimeException {
    public InvalidPriceException(String message) {
        super(message);
    }
}
