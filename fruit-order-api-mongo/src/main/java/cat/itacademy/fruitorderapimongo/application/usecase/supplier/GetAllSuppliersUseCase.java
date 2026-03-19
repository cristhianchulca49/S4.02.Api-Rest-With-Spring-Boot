package cat.itacademy.fruitorderapimongo.application.usecase.supplier;

import cat.itacademy.fruitorderapimongo.domain.model.Supplier;
import cat.itacademy.fruitorderapimongo.domain.port.out.SupplierRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GetAllSuppliersUseCase {
    private final SupplierRepositoryPort supplierRepositoryPort;

    public GetAllSuppliersUseCase(SupplierRepositoryPort supplierRepositoryPort) {
        this.supplierRepositoryPort = supplierRepositoryPort;
    }

    @Transactional(readOnly = true)
    public List<Supplier> execute() {
        return supplierRepositoryPort.findAll();
    }
}
