package cat.itacademy.fruitapih2.exception;

public class FruitNotFoundException extends RuntimeException {
    public FruitNotFoundException(Long id) {
        super(String.format("Fruit with id: %s not found", id.toString()));
    }
}
