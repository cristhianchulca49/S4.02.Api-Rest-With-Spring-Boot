package cat.itacademy.fruitorderapimongo.repository;

import cat.itacademy.fruitorderapimongo.model.Fruit;
import cat.itacademy.fruitorderapimongo.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface FruitRepository extends JpaRepository<Fruit, String> {
    boolean existsByName(String name);

    List<Fruit> findAllBySupplier(Supplier supplier);

    boolean existsBySupplier(Supplier supplier);
}
