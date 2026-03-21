package cat.itacademy.fruitorderapimongo.application.usecase.supplier;

import cat.itacademy.fruitorderapimongo.domain.model.Supplier;
import cat.itacademy.fruitorderapimongo.domain.port.out.FruitRepositoryPort;
import cat.itacademy.fruitorderapimongo.domain.port.out.SupplierRepositoryPort;
import cat.itacademy.fruitorderapimongo.domain.exception.ResourceHasDependenciesException;
import cat.itacademy.fruitorderapimongo.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteSupplierUseCase {
    private final SupplierRepositoryPort supplierRepositoryPort;
    private final FruitRepositoryPort fruitRepositoryPort;

    public DeleteSupplierUseCase(SupplierRepositoryPort supplierRepositoryPort, FruitRepositoryPort fruitRepositoryPort) {
        this.supplierRepositoryPort = supplierRepositoryPort;
        this.fruitRepositoryPort = fruitRepositoryPort;
    }

    @Transactional
    public void execute(String id) {
        Supplier supplier = supplierRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", id));
        if (!fruitRepositoryPort.findBySupplierId(id).isEmpty()) {
            throw new ResourceHasDependenciesException("Supplier", id);
        }
        supplierRepositoryPort.delete(supplier);
    }
}
