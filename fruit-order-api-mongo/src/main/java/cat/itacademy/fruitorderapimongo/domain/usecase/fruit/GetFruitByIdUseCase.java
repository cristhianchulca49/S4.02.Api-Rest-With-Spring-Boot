package cat.itacademy.fruitorderapimongo.domain.usecase.fruit;

import cat.itacademy.fruitorderapimongo.domain.model.Fruit;
import cat.itacademy.fruitorderapimongo.domain.port.out.FruitRepositoryPort;
import cat.itacademy.fruitorderapimongo.infrastructure.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetFruitByIdUseCase {
    private final FruitRepositoryPort fruitRepositoryPort;

    public GetFruitByIdUseCase(FruitRepositoryPort fruitRepositoryPort) {
        this.fruitRepositoryPort = fruitRepositoryPort;
    }

    @Transactional(readOnly = true)
    public Fruit execute(String id) {
        return fruitRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fruit", id));
    }
}
