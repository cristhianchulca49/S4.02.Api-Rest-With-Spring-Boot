package cat.itacademy.fruitorderapimongo.domain.port.out;

import cat.itacademy.fruitorderapimongo.domain.model.Fruit;

import java.util.List;
import java.util.Optional;

public interface FruitRepositoryPort {
    Fruit save(Fruit fruit);
    Optional<Fruit> findById(String id);
    List<Fruit> findAll();
    boolean existsByName(String name);
    void delete(Fruit fruit);
    List<Fruit> findBySupplierId(String supplierId);
    List<Fruit> findAllById(List<String> fruitsId);
}
