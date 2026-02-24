package cat.itacademy.fruitapimysql.repository;

import cat.itacademy.fruitapimysql.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    boolean existsByName(String name);
}
