package cat.itacademy.fruitorderapimongo.domain.usecase.fruit;

import cat.itacademy.fruitorderapimongo.domain.model.Fruit;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.Name;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.PricePerKg;
import cat.itacademy.fruitorderapimongo.domain.port.out.FruitRepositoryPort;
import cat.itacademy.fruitorderapimongo.infrastructure.exception.ResourceAlreadyExistsException;
import cat.itacademy.fruitorderapimongo.infrastructure.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateFruitUseCase {
    private final FruitRepositoryPort fruitRepositoryPort;

    public UpdateFruitUseCase(FruitRepositoryPort fruitRepositoryPort) {
        this.fruitRepositoryPort = fruitRepositoryPort;
    }

    @Transactional
    public Fruit execute(String id, String name, Double weightKg) {
        Fruit fruit = fruitRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fruit", id));

        if (!fruit.getName().getValue().equalsIgnoreCase(name) && fruitRepositoryPort.existsByName(name)) {
            throw new ResourceAlreadyExistsException("Fruit", name);
        }

        fruit.changeName(Name.of(name));
        fruit.changePrice(PricePerKg.of(weightKg));
        return fruitRepositoryPort.save(fruit);
    }
}
