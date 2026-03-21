package cat.itacademy.fruitorderapimongo.infrastructure.adapter.out.persistence;

import cat.itacademy.fruitorderapimongo.domain.model.Supplier;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.Name;
import cat.itacademy.fruitorderapimongo.domain.port.out.SupplierRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MongoSupplierAdapter implements SupplierRepositoryPort {
    private final SupplierRepository supplierRepository;

    public MongoSupplierAdapter(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public Supplier save(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @Override
    public Optional<Supplier> findById(String id) {
        return supplierRepository.findById(id);
    }

    @Override
    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }

    @Override
    public boolean existsByName(String name) {
        return supplierRepository.existsByName(Name.of(name));
    }

    @Override
    public void delete(Supplier supplier) {
        supplierRepository.delete(supplier);
    }

    @Override
    public boolean existsById(String supplierId) {
        return supplierRepository.existsById(supplierId);
    }
}
