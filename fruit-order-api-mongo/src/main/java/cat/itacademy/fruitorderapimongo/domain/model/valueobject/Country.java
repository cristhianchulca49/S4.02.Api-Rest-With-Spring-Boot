package cat.itacademy.fruitorderapimongo.domain.model.valueobject;

import cat.itacademy.fruitorderapimongo.infrastructure.exception.InvalidNameException;
import lombok.Getter;

@Getter
public class Country {

    private final String value;

    private Country(String value) {
        this.value = value;
    }

    public static Country of(String value) {
        if (value == null) {
            throw new InvalidNameException("Country cannot be null");
        }
        if (value.isBlank()) {
            throw new InvalidNameException("Country cannot be blank");
        }
        return new Country(value);
    }
}
