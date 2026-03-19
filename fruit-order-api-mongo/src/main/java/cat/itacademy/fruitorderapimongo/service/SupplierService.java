package cat.itacademy.fruitorderapimongo.service;

import cat.itacademy.fruitorderapimongo.infrastructure.dto.supplier.SupplierDtoRequest;
import cat.itacademy.fruitorderapimongo.infrastructure.dto.supplier.SupplierDtoResponse;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.Country;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.Name;
import cat.itacademy.fruitorderapimongo.infrastructure.exception.ResourceAlreadyExistsException;
import cat.itacademy.fruitorderapimongo.infrastructure.exception.ResourceHasDependenciesException;
import cat.itacademy.fruitorderapimongo.infrastructure.exception.ResourceNotFoundException;
import cat.itacademy.fruitorderapimongo.infrastructure.mapper.SupplierMapper;
import cat.itacademy.fruitorderapimongo.domain.model.Supplier;
import cat.itacademy.fruitorderapimongo.infrastructure.adapter.out.persistence.FruitRepository;
import cat.itacademy.fruitorderapimongo.infrastructure.adapter.out.persistence.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final FruitRepository fruitRepository;

    public SupplierService(SupplierRepository supplierRepository, FruitRepository fruitRepository) {
        this.supplierRepository = supplierRepository;
        this.fruitRepository = fruitRepository;
    }

    @Transactional
    public SupplierDtoResponse create(SupplierDtoRequest supplierDtoRequest) {
        Name supplierName = Name.of(supplierDtoRequest.name());
        if (supplierRepository.existsByName(supplierName)) {
            throw new ResourceAlreadyExistsException("Supplier", supplierDtoRequest.name());
        }
        Supplier supplier = supplierRepository.save(SupplierMapper.toEntity(supplierDtoRequest));
        return SupplierMapper.toDto(supplier);
    }

    @Transactional
    public SupplierDtoResponse update(String id, SupplierDtoRequest supplierDtoRequest) {
        Supplier supplier = verifySupplierExists(id);

        Name supplierName = Name.of(supplierDtoRequest.name());
        if (!supplier.getName().getValue().equalsIgnoreCase(supplierDtoRequest.name()) &&
                supplierRepository.existsByName(supplierName)) {
            throw new ResourceAlreadyExistsException("Supplier", supplierDtoRequest.name());
        }

        supplier.changeName(supplierName);
        supplier.changeCountry(Country.of(supplierDtoRequest.country()));
        return SupplierMapper.toDto(supplierRepository.save(supplier));
    }

    public void delete(String id) {
        Supplier supplier = verifySupplierExists(id);
        if (fruitRepository.existsBySupplier(supplier)) {
            throw new ResourceHasDependenciesException("Supplier", id);
        }
    }

    private Supplier verifySupplierExists(String id) {
        return supplierRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Supplier", id));
    }
}
