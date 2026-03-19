package cat.itacademy.fruitorderapimongo.exception;

public class ResourceHasDependenciesException extends RuntimeException {
    public ResourceHasDependenciesException(String nameResource, String id) {
        super(String.format("%s with id: %s has fruits associated", nameResource, id));
    }
}
