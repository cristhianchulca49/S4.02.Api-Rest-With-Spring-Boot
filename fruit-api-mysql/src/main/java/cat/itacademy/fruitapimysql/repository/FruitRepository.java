package cat.itacademy.fruitapimysql.repository;

import cat.itacademy.fruitapimysql.model.Fruit;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FruitRepository extends JpaRepository<Fruit, Long> {
    boolean existsByName(String name);

}
