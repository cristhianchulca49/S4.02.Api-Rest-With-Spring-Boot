package cat.itacademy.fruitapimysql.repository;

import cat.itacademy.fruitapimysql.model.Fruit;
import cat.itacademy.fruitapimysql.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;


public interface FruitRepository extends JpaRepository<Fruit, Long> {
    boolean existsByName(String name);

    List<Fruit> findAllBySupplier(Supplier supplier);

    boolean existsBySupplier(Supplier supplier);
}
