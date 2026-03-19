package cat.itacademy.fruitorderapimongo.infrastructure.adapter.out.persistence;

import cat.itacademy.fruitorderapimongo.domain.model.Fruit;
import cat.itacademy.fruitorderapimongo.domain.model.Supplier;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.Name;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface FruitRepository extends MongoRepository<Fruit, String> {
    boolean existsByName(Name name);

    List<Fruit> findAllBySupplier(Supplier supplier);

    boolean existsBySupplier(Supplier supplier);
}
