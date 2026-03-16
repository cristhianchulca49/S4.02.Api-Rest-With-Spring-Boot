package cat.itacademy.fruitapimysql.service;

import cat.itacademy.fruitapimysql.dto.supplier.SupplierDtoRequest;
import cat.itacademy.fruitapimysql.dto.supplier.SupplierDtoResponse;
import cat.itacademy.fruitapimysql.exception.ResourceAlreadyExistsException;
import cat.itacademy.fruitapimysql.mapper.SupplierMapper;
import cat.itacademy.fruitapimysql.model.Supplier;
import cat.itacademy.fruitapimysql.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SupplierService {
    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Transactional
    public SupplierDtoResponse create(SupplierDtoRequest supplierDtoRequest) {
        if (supplierRepository.existsByName(supplierDtoRequest.name())) {
            throw new ResourceAlreadyExistsException("Supplier", supplierDtoRequest.name());
        }
        Supplier supplier = supplierRepository.save(SupplierMapper.toEntity(supplierDtoRequest));
        return SupplierMapper.toDto(supplier);
    }
}
