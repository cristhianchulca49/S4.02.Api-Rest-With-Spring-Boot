package cat.itacademy.fruitorderapimongo.domain.usecase.fruit;

import cat.itacademy.fruitorderapimongo.domain.model.Fruit;
import cat.itacademy.fruitorderapimongo.domain.port.out.FruitRepositoryPort;
import cat.itacademy.fruitorderapimongo.infrastructure.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteFruitUseCase {
    private final FruitRepositoryPort fruitRepositoryPort;

    public DeleteFruitUseCase(FruitRepositoryPort fruitRepositoryPort) {
        this.fruitRepositoryPort = fruitRepositoryPort;
    }

    @Transactional
    public void execute(String id) {
        Fruit fruit = fruitRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fruit", id));
        fruitRepositoryPort.delete(fruit);
    }
}
