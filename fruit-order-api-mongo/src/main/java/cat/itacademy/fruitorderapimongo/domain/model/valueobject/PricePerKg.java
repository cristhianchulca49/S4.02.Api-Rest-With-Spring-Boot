package cat.itacademy.fruitorderapimongo.domain.model.valueobject;

import cat.itacademy.fruitorderapimongo.infrastructure.exception.InvalidPriceException;
import lombok.Getter;

@Getter
public class PricePerKg {

    private final Double value;

    private PricePerKg(Double value) {
        this.value = value;
    }

    public static PricePerKg of(Double value) {
        if (value == null) {
            throw new InvalidPriceException("Price cannot be null");
        }
        if (value <= 0) {
            throw new InvalidPriceException("Price must be greater than zero");
        }
        return new PricePerKg(value);
    }
}
