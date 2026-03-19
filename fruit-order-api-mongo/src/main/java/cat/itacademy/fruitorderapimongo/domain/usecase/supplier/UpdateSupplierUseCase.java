package cat.itacademy.fruitorderapimongo.domain.usecase.supplier;

import cat.itacademy.fruitorderapimongo.domain.model.Supplier;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.Country;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.Name;
import cat.itacademy.fruitorderapimongo.domain.port.out.SupplierRepositoryPort;
import cat.itacademy.fruitorderapimongo.infrastructure.exception.ResourceAlreadyExistsException;
import cat.itacademy.fruitorderapimongo.infrastructure.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateSupplierUseCase {
    private final SupplierRepositoryPort supplierRepositoryPort;

    public UpdateSupplierUseCase(SupplierRepositoryPort supplierRepositoryPort) {
        this.supplierRepositoryPort = supplierRepositoryPort;
    }

    @Transactional
    public Supplier execute(String id, String name, String country) {
        Supplier supplier = supplierRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", id));

        if (!supplier.getName().getValue().equalsIgnoreCase(name) && supplierRepositoryPort.existsByName(name)) {
            throw new ResourceAlreadyExistsException("Supplier", name);
        }

        supplier.changeName(Name.of(name));
        supplier.changeCountry(Country.of(country));
        return supplierRepositoryPort.save(supplier);
    }
}
