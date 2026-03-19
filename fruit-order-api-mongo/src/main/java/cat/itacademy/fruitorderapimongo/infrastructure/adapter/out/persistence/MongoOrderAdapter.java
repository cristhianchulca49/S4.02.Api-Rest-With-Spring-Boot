package cat.itacademy.fruitorderapimongo.infrastructure.adapter.out.persistence;

import cat.itacademy.fruitorderapimongo.domain.model.order.Order;
import cat.itacademy.fruitorderapimongo.domain.port.out.OrderRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MongoOrderAdapter implements OrderRepositoryPort {
    private final OrderRepository orderRepository;

    public MongoOrderAdapter(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> findById(String id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }
}
