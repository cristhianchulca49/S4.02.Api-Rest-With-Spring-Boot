package cat.itacademy.fruitorderapimongo.domain.usecase.order;

import cat.itacademy.fruitorderapimongo.domain.model.order.Order;
import cat.itacademy.fruitorderapimongo.domain.port.out.OrderRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GetAllOrdersUseCase {
    private final OrderRepositoryPort orderRepositoryPort;

    public GetAllOrdersUseCase(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
    }

    @Transactional(readOnly = true)
    public List<Order> execute() {
        return orderRepositoryPort.findAll();
    }
}
