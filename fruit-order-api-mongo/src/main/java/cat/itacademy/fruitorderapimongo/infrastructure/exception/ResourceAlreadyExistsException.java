package cat.itacademy.fruitorderapimongo.infrastructure.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String resourceName, String fieldName ) {
        super(String.format("%s with name %s already exists", resourceName, fieldName));
    }
}
