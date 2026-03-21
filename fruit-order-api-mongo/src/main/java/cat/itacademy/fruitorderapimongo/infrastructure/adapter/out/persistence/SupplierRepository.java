package cat.itacademy.fruitorderapimongo.infrastructure.adapter.out.persistence;

import cat.itacademy.fruitorderapimongo.domain.model.Supplier;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.Name;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SupplierRepository extends MongoRepository<Supplier, String> {

    boolean existsByName(Name name);
}
