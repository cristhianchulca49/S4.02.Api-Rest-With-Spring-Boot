package cat.itacademy.fruitorderapimongo.domain.port.out;

import cat.itacademy.fruitorderapimongo.domain.model.Supplier;

import java.util.List;
import java.util.Optional;

public interface SupplierRepositoryPort {
    Supplier save(Supplier supplier);
    Optional<Supplier> findById(String id);
    List<Supplier> findAll();
    boolean existsByName(String name);
    void delete(Supplier supplier);
    boolean existsById(String supplierId);
}
