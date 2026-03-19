package cat.itacademy.fruitorderapimongo.domain.model.valueobject;

import cat.itacademy.fruitorderapimongo.domain.exception.InvalidDateException;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DataOrder {
    private final LocalDate deliveryDate;

    private DataOrder(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public static DataOrder of(LocalDate deliveryDate) {
        if (deliveryDate == null) {
            throw new InvalidDateException("Date cannot be null");
        }
        if (!deliveryDate.isAfter(LocalDate.now())) {
            throw new InvalidDateException("Date must be at least tomorrow");
        }
        return new DataOrder(deliveryDate);
    }
}
