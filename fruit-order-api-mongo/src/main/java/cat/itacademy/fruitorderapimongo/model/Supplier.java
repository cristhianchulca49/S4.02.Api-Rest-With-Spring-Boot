package cat.itacademy.fruitorderapimongo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "suppliers")
@Getter

@NoArgsConstructor
public class Supplier {

    @Id
    private String id;

    @Setter
    private String name;

    @Setter
    private String country;

    public Supplier(String name, String country) {
        this.name = name;
        this.country = country;
    }
}