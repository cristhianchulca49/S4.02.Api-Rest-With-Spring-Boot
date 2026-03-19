package cat.itacademy.fruitorderapimongo.domain.model.valueobject;

import cat.itacademy.fruitorderapimongo.domain.exception.InvalidNameException;
import lombok.Getter;

@Getter
public class Name {

    private final String value;

    private Name(String value) {
        this.value = value;
    }

    public static Name of(String value) {
        if (value == null) {
            throw new InvalidNameException("Name cannot be null");
        }
        if (value.isBlank()) {
            throw new InvalidNameException("Name cannot be blank");
        }
        return new Name(value);
    }
}
