package cat.itacademy.fruitorderapimongo.domain.usecase.supplier;

import cat.itacademy.fruitorderapimongo.domain.model.Supplier;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.Country;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.Name;
import cat.itacademy.fruitorderapimongo.domain.port.out.SupplierRepositoryPort;
import cat.itacademy.fruitorderapimongo.infrastructure.exception.ResourceAlreadyExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateSupplierUseCase {
    private final SupplierRepositoryPort supplierRepositoryPort;

    public CreateSupplierUseCase(SupplierRepositoryPort supplierRepositoryPort) {
        this.supplierRepositoryPort = supplierRepositoryPort;
    }

    @Transactional
    public Supplier execute(String name, String country) {
        Name supplierName = Name.of(name);
        if (supplierRepositoryPort.existsByName(supplierName.getValue())) {
            throw new ResourceAlreadyExistsException("Supplier", name);
        }
        return supplierRepositoryPort.save(new Supplier(supplierName, Country.of(country)));
    }
}
