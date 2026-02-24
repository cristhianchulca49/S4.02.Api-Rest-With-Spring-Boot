package cat.itacademy.fruitapimysql.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Long id) {
        super(String.format("Fruit with id: %s not found", id.toString()));
    }
}
