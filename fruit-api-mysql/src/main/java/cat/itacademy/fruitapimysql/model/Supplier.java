package cat.itacademy.fruitapimysql.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Entity
@Table(name = "Supplier")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "name")
    private String name;

    @Setter
    @Column(name = "country")
    private String country;

    @OneToMany(mappedBy = "supplier")
    @JsonIgnore
    private List<Fruit> fruits;

    public Supplier(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public Supplier() {

    }
}
