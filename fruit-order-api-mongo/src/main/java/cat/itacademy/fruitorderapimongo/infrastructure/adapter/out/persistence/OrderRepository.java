package cat.itacademy.fruitorderapimongo.infrastructure.adapter.out.persistence;

import cat.itacademy.fruitorderapimongo.domain.model.order.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}
