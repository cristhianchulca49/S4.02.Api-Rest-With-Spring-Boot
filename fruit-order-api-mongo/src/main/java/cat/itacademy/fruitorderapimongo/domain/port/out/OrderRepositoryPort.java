package cat.itacademy.fruitorderapimongo.domain.port.out;

import cat.itacademy.fruitorderapimongo.domain.model.order.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryPort {
    Order save(Order order);
    Optional<Order> findById(String id);
    List<Order> findAll();
}
