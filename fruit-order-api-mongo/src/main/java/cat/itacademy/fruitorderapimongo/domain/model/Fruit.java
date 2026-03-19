package cat.itacademy.fruitorderapimongo.domain.model;

import cat.itacademy.fruitorderapimongo.domain.model.valueobject.Name;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.PricePerKg;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "fruits")
@Getter
@NoArgsConstructor
public class Fruit {

    @Id
    private String id;
    private Name name;
    private PricePerKg price;
    private Supplier supplier;

    public Fruit(Name name, PricePerKg price, Supplier supplier) {
        this.name = name;
        this.price = price;
        this.supplier = supplier;
    }

    public void changeName(Name name) {
        this.name = name;
    }

    public void changePrice(PricePerKg price) {
        this.price = price;
    }
}
