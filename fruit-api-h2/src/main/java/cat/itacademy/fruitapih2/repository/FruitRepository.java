package cat.itacademy.fruitapih2.repository;

import cat.itacademy.fruitapih2.model.Fruit;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FruitRepository extends JpaRepository<Fruit, Long> {
    boolean existsByName(String name);

}
