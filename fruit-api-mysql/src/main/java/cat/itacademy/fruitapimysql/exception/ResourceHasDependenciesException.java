package cat.itacademy.fruitapimysql.exception;

public class ResourceHasDependenciesException extends RuntimeException {
    public ResourceHasDependenciesException(String nameResource, Long id) {
        super(String.format("%s with id: %d has fruits associated", nameResource, id));
    }
}
