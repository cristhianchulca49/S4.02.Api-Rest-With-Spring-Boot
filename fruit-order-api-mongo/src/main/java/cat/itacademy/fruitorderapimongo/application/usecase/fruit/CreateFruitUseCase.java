package cat.itacademy.fruitorderapimongo.application.usecase.fruit;

import cat.itacademy.fruitorderapimongo.domain.model.Fruit;
import cat.itacademy.fruitorderapimongo.domain.model.Supplier;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.Name;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.PricePerKg;
import cat.itacademy.fruitorderapimongo.domain.port.out.FruitRepositoryPort;
import cat.itacademy.fruitorderapimongo.domain.port.out.SupplierRepositoryPort;
import cat.itacademy.fruitorderapimongo.domain.exception.ResourceAlreadyExistsException;
import cat.itacademy.fruitorderapimongo.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateFruitUseCase {
    private final FruitRepositoryPort fruitRepositoryPort;
    private final SupplierRepositoryPort supplierRepositoryPort;

    public CreateFruitUseCase(FruitRepositoryPort fruitRepositoryPort, SupplierRepositoryPort supplierRepositoryPort) {
        this.fruitRepositoryPort = fruitRepositoryPort;
        this.supplierRepositoryPort = supplierRepositoryPort;
    }

    @Transactional
    public Fruit execute(String name, Double weightKg, String supplierId) {
        Name fruitName = Name.of(name);
        if (fruitRepositoryPort.existsByName(fruitName.getValue())) {
            throw new ResourceAlreadyExistsException("Fruit", name);
        }

        Supplier supplier = supplierRepositoryPort.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", supplierId));

        return fruitRepositoryPort.save(new Fruit(fruitName, PricePerKg.of(weightKg), supplier));
    }
}
