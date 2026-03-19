package cat.itacademy.fruitorderapimongo.domain.usecase.order;

import cat.itacademy.fruitorderapimongo.domain.model.order.Order;
import cat.itacademy.fruitorderapimongo.domain.port.out.OrderRepositoryPort;
import cat.itacademy.fruitorderapimongo.infrastructure.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetOrderByIdUseCase {
    private final OrderRepositoryPort orderRepositoryPort;

    public GetOrderByIdUseCase(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
    }

    @Transactional(readOnly = true)
    public Order execute(String id) {
        return orderRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));
    }
}
