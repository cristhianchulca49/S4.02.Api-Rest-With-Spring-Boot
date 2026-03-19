package cat.itacademy.fruitorderapimongo.model.order;

import cat.itacademy.fruitorderapimongo.model.Fruit;
import lombok.Getter;
import lombok.Setter;

@Getter
public class OrderItem {
    @Setter
    private Fruit fruit;
    @Setter
    private Double quantityInKg;

    public OrderItem(Fruit fruit, Double quantityInKg) {
        this.fruit = fruit;
        this.quantityInKg = quantityInKg;
    }
}
