package cat.itacademy.fruitorderapimongo.repository;

import cat.itacademy.fruitorderapimongo.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, String> {

    boolean existsByName(String name);
}
