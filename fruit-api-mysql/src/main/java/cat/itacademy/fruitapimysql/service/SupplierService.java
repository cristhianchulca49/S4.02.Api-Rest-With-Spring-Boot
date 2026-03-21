package cat.itacademy.fruitapimysql.service;

import cat.itacademy.fruitapimysql.dto.supplier.SupplierDtoRequest;
import cat.itacademy.fruitapimysql.dto.supplier.SupplierDtoResponse;
import cat.itacademy.fruitapimysql.exception.ResourceAlreadyExistsException;
import cat.itacademy.fruitapimysql.exception.ResourceHasDependenciesException;
import cat.itacademy.fruitapimysql.exception.ResourceNotFoundException;
import cat.itacademy.fruitapimysql.mapper.SupplierMapper;
import cat.itacademy.fruitapimysql.model.Supplier;
import cat.itacademy.fruitapimysql.repository.FruitRepository;
import cat.itacademy.fruitapimysql.repository.SupplierRepository;
import jakarta.validation.Valid;
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
    public SupplierDtoResponse update(Long id, SupplierDtoRequest supplierDtoRequest) {
        Supplier supplier = verifySupplierExists(id);

        if (!supplier.getName().equalsIgnoreCase(supplierDtoRequest.name()) &&
                supplierRepository.existsByName(supplierDtoRequest.name())) {
            throw new ResourceAlreadyExistsException("Supplier", supplierDtoRequest.name());
        }

        supplier.setName(supplierDtoRequest.name());
        supplier.setCountry(supplierDtoRequest.country());
        return SupplierMapper.toDto(supplierRepository.save(supplier));
    }

    public void delete(Long id) {
        Supplier supplier = verifySupplierExists(id);
        if (fruitRepository.existsBySupplier(supplier)) {
            throw new ResourceHasDependenciesException("Supplier", id);
        }
    }

    private Supplier verifySupplierExists(Long id) {
        return supplierRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Supplier", id));
    }
}
