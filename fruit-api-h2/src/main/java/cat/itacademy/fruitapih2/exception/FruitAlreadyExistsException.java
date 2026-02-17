package cat.itacademy.fruitapih2.exception;

public class FruitAlreadyExistsException extends RuntimeException {
    public FruitAlreadyExistsException(String name) {
        super(String.format("Fruit called %s already exists", name));
    }
}
