package cat.itacademy.fruitorderapimongo.application.usecase.order;

import cat.itacademy.fruitorderapimongo.domain.model.Fruit;
import cat.itacademy.fruitorderapimongo.domain.model.order.Order;
import cat.itacademy.fruitorderapimongo.domain.model.order.OrderItem;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.DataOrder;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.Name;
import cat.itacademy.fruitorderapimongo.domain.port.out.FruitRepositoryPort;
import cat.itacademy.fruitorderapimongo.domain.port.out.OrderRepositoryPort;
import cat.itacademy.fruitorderapimongo.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        List<String> fruitsId = items.stream()
                .map(OrderItemInput::fruitId)
                .distinct()
                .toList();

        Map<String, Fruit> fruitsFounded = fruitRepositoryPort.findAllById(fruitsId)
                .stream().collect(Collectors.toMap(Fruit::getId, fruit -> fruit));

        fruitsId.forEach(id -> {
            if (!fruitsFounded.containsKey(id)) {
                throw new ResourceNotFoundException("Fruit", id);
            }
        });

        List<OrderItem> orderItems = items.stream()
                .map(item -> new OrderItem(fruitsFounded.get(item.fruitId()), item.quantityInKg()))
                .toList();

        Order order = new Order(Name.of(clientName), DataOrder.of(deliveryDate), orderItems );
        return orderRepositoryPort.save(order);
    }

    public record OrderItemInput(String fruitId, Double quantityInKg) {
    }
}
