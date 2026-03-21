package cat.itacademy.fruitorderapimongo.application.usecase.supplier;

import cat.itacademy.fruitorderapimongo.domain.model.Supplier;
import cat.itacademy.fruitorderapimongo.domain.port.out.SupplierRepositoryPort;
import cat.itacademy.fruitorderapimongo.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetSupplierByIdUseCase {
    private final SupplierRepositoryPort supplierRepositoryPort;

    public GetSupplierByIdUseCase(SupplierRepositoryPort supplierRepositoryPort) {
        this.supplierRepositoryPort = supplierRepositoryPort;
    }

    @Transactional(readOnly = true)
    public Supplier execute(String id) {
        return supplierRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", id));
    }
}
