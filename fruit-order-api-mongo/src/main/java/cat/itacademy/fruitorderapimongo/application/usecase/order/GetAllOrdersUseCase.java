package cat.itacademy.fruitorderapimongo.application.usecase.order;

import cat.itacademy.fruitorderapimongo.domain.model.order.Order;
import cat.itacademy.fruitorderapimongo.domain.port.out.OrderRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllOrdersUseCase {
    private final OrderRepositoryPort orderRepository;

    public GetAllOrdersUseCase(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> execute() {
        return orderRepository.findAll();
    }
}
