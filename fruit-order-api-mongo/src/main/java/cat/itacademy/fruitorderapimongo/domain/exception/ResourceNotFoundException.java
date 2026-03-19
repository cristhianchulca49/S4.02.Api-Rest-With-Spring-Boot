package cat.itacademy.fruitorderapimongo.domain.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, String id) {
        super(String.format("%s with id: %s not found", resourceName, id));
    }
}
