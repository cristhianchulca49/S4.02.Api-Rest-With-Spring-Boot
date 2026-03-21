package cat.itacademy.fruitorderapimongo.domain.model;

import cat.itacademy.fruitorderapimongo.domain.model.valueobject.Country;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.Name;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "suppliers")
@Getter

@NoArgsConstructor
public class Supplier {

    @Id
    private String id;

    private Name name;

    private Country country;

    public Supplier(Name name, Country country) {
        this.name = name;
        this.country = country;
    }

    public void changeName(Name name) {
        this.name = name;
    }

    public void changeCountry(Country country) {
        this.country = country;
    }
}
