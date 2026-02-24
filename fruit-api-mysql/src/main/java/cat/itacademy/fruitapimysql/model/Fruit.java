package cat.itacademy.fruitapimysql.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "fruits")
public class Fruit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, unique = true)
    private String name;

    @Setter
    @Column(nullable = false)
    private Double weightKg;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id")
    private Supplier supplier;

    public Fruit(String name, Double weightKg, Supplier suppliers) {
        this.name = name;
        this.weightKg = weightKg;
        this.supplier = suppliers;
    }

    public Fruit() {

    }

}
