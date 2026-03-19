package cat.itacademy.fruitorderapimongo.service;

import cat.itacademy.fruitorderapimongo.dto.supplier.SupplierDtoRequest;
import cat.itacademy.fruitorderapimongo.dto.supplier.SupplierDtoResponse;
import cat.itacademy.fruitorderapimongo.exception.ResourceAlreadyExistsException;
import cat.itacademy.fruitorderapimongo.exception.ResourceHasDependenciesException;
import cat.itacademy.fruitorderapimongo.exception.ResourceNotFoundException;
import cat.itacademy.fruitorderapimongo.mapper.SupplierMapper;
import cat.itacademy.fruitorderapimongo.model.Supplier;
import cat.itacademy.fruitorderapimongo.repository.FruitRepository;
import cat.itacademy.fruitorderapimongo.repository.SupplierRepository;
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
        if (supplierRepository.existsByName(supplierDtoRequest.name())) {
            throw new ResourceAlreadyExistsException("Supplier", supplierDtoRequest.name());
        }
        Supplier supplier = supplierRepository.save(SupplierMapper.toEntity(supplierDtoRequest));
        return SupplierMapper.toDto(supplier);
    }

    @Transactional
    public SupplierDtoResponse update(String id, SupplierDtoRequest supplierDtoRequest) {
        Supplier supplier = verifySupplierExists(id);

        if (!supplier.getName().equalsIgnoreCase(supplierDtoRequest.name()) &&
                supplierRepository.existsByName(supplierDtoRequest.name())) {
            throw new ResourceAlreadyExistsException("Supplier", supplierDtoRequest.name());
        }

        supplier.setName(supplierDtoRequest.name());
        supplier.setCountry(supplierDtoRequest.country());
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