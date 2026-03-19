package cat.itacademy.fruitorderapimongo.domain.usecase.order;

import cat.itacademy.fruitorderapimongo.domain.model.Fruit;
import cat.itacademy.fruitorderapimongo.domain.model.order.Order;
import cat.itacademy.fruitorderapimongo.domain.model.order.OrderItem;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.DataOrder;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.Name;
import cat.itacademy.fruitorderapimongo.domain.port.out.FruitRepositoryPort;
import cat.itacademy.fruitorderapimongo.domain.port.out.OrderRepositoryPort;
import cat.itacademy.fruitorderapimongo.infrastructure.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CreateOrderUseCase {
    private final OrderRepositoryPort orderRepositoryPort;
    private final FruitRepositoryPort fruitRepositoryPort;

    public CreateOrderUseCase(OrderRepositoryPort orderRepositoryPort, FruitRepositoryPort fruitRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
        this.fruitRepositoryPort = fruitRepositoryPort;
    }

    @Transactional
    public Order execute(String clientName, LocalDate deliveryDate, List<OrderItemInput> items) {
        List<OrderItem> orderItems = items.stream()
                .map(item -> new OrderItem(resolveFruit(item.fruitId()), item.quantityInKg()))
                .toList();

        Order order = new Order(Name.of(clientName), DataOrder.of(deliveryDate), orderItems);
        return orderRepositoryPort.save(order);
    }

    private Fruit resolveFruit(String fruitId) {
        return fruitRepositoryPort.findById(fruitId)
                .orElseThrow(() -> new ResourceNotFoundException("Fruit", fruitId));
    }

    public record OrderItemInput(String fruitId, Double quantityInKg) {
    }
}
