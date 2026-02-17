package cat.itacademy.fruitapih2.model;

import jakarta.persistence.*;
import lombok.Setter;

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

    public Fruit(String name, Double weightKg) {
        this.name = name;
        this.weightKg = weightKg;
    }

    public Fruit() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getWeightKg() {
        return weightKg;
    }
}
