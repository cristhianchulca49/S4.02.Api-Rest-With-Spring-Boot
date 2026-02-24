package cat.itacademy.fruitapimysql.service;

import cat.itacademy.fruitapimysql.dto.SupplierDto;
import cat.itacademy.fruitapimysql.exception.ResourceAlreadyExistsException;
import cat.itacademy.fruitapimysql.mapper.SupplierMapper;
import cat.itacademy.fruitapimysql.model.Supplier;
import cat.itacademy.fruitapimysql.repository.SupplierRepository;
import org.springframework.stereotype.Service;

@Service
public class SupplierService {
    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public SupplierDto create(SupplierDto supplierDto) {
        if (supplierRepository.existsByName(supplierDto.name())) {
            throw new ResourceAlreadyExistsException("Supplier", supplierDto.name());
        }
        Supplier supplier = supplierRepository.save(SupplierMapper.toEntity(supplierDto));
        return SupplierMapper.toDto(supplier);
    }
}
