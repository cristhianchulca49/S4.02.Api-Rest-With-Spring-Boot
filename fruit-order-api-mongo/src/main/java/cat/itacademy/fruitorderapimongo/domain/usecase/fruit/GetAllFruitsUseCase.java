package cat.itacademy.fruitorderapimongo.domain.usecase.fruit;

import cat.itacademy.fruitorderapimongo.domain.model.Fruit;
import cat.itacademy.fruitorderapimongo.domain.port.out.FruitRepositoryPort;
import cat.itacademy.fruitorderapimongo.domain.port.out.SupplierRepositoryPort;
import cat.itacademy.fruitorderapimongo.infrastructure.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GetAllFruitsUseCase {
    private final FruitRepositoryPort fruitRepositoryPort;
    private final SupplierRepositoryPort supplierRepositoryPort;

    public GetAllFruitsUseCase(FruitRepositoryPort fruitRepositoryPort, SupplierRepositoryPort supplierRepositoryPort) {
        this.fruitRepositoryPort = fruitRepositoryPort;
        this.supplierRepositoryPort = supplierRepositoryPort;
    }

    @Transactional(readOnly = true)
    public List<Fruit> execute(Optional<String> supplierId) {
        if (supplierId.isPresent()) {
            String id = supplierId.get();
            supplierRepositoryPort.findById(id).orElseThrow(() -> new ResourceNotFoundException("Supplier", id));
            return fruitRepositoryPort.findBySupplierId(id);
        }
        return fruitRepositoryPort.findAll();
    }
}
