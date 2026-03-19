package cat.itacademy.fruitorderapimongo.application.usecase.order;

import cat.itacademy.fruitorderapimongo.domain.exception.ResourceNotFoundException;
import cat.itacademy.fruitorderapimongo.domain.model.order.Order;
import cat.itacademy.fruitorderapimongo.domain.port.out.OrderRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class GetOrderByIdUseCase {
    private final OrderRepositoryPort orderRepository;

    public GetOrderByIdUseCase(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order execute(String id) {
        return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", id));
    }
}
