package cat.itacademy.fruitorderapimongo.model.order;

import cat.itacademy.fruitorderapimongo.exception.InvalidDateException;
import lombok.Getter;

import java.time.LocalDate;
@Getter
public class DataOrder {
    private final LocalDate deliveryDate;

    DataOrder(LocalDate date) {
        if (date == null) {
            throw new InvalidDateException("Date cannot be null");
        }
        if (!date.isAfter(LocalDate.now())) {
            throw new InvalidDateException("Date must be at least tomorrow");
        }
        this.deliveryDate = date;
    }
}
