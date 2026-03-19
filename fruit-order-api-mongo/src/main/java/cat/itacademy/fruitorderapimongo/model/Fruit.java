package cat.itacademy.fruitorderapimongo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "fruits")
@Getter
@NoArgsConstructor
public class Fruit {

    @Id
    private String id;
    @Setter
    private String name;
    @Setter
    private Double weightKg;
    @Setter
    private Supplier supplier;

    public Fruit(String name, double weightKg, Supplier supplier) {
        this.name = name;
        this.weightKg = weightKg;
        this.supplier = supplier;
    }
}