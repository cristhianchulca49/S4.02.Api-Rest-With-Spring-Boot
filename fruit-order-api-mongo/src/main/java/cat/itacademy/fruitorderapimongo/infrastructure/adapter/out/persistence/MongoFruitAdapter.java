package cat.itacademy.fruitorderapimongo.infrastructure.adapter.out.persistence;

import cat.itacademy.fruitorderapimongo.domain.model.Fruit;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.Name;
import cat.itacademy.fruitorderapimongo.domain.port.out.FruitRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MongoFruitAdapter implements FruitRepositoryPort {
    private final FruitRepository fruitRepository;

    public MongoFruitAdapter(FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository;
    }

    @Override
    public Fruit save(Fruit fruit) {
        return fruitRepository.save(fruit);
    }

    @Override
    public Optional<Fruit> findById(String id) {
        return fruitRepository.findById(id);
    }

    @Override
    public List<Fruit> findAll() {
        return fruitRepository.findAll();
    }

    @Override
    public boolean existsByName(String name) {
        return fruitRepository.existsByName(Name.of(name));
    }

    @Override
    public void delete(Fruit fruit) {
        fruitRepository.delete(fruit);
    }

    @Override
    public List<Fruit> findBySupplierId(String supplierId) {
        return fruitRepository.findAllBySupplier_Id(supplierId);
    }
}
