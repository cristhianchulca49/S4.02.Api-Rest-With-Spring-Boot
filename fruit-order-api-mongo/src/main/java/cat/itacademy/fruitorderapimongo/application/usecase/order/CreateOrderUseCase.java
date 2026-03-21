package cat.itacademy.fruitorderapimongo.application.usecase.order;

import cat.itacademy.fruitorderapimongo.application.usecase.order.command.OrderCommand;
import cat.itacademy.fruitorderapimongo.domain.model.order.Order;
import cat.itacademy.fruitorderapimongo.domain.model.order.OrderItem;
import cat.itacademy.fruitorderapimongo.domain.port.out.OrderRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreateOrderUseCase {
    private final OrderRepositoryPort orderRepository;
    private final FruitResolver fruitResolver;

    public CreateOrderUseCase(OrderRepositoryPort orderRepository, FruitResolver fruitResolver) {
        this.orderRepository = orderRepository;
        this.fruitResolver = fruitResolver;
    }

    public Order execute(OrderCommand orderCommand) {
        List<OrderItem> orderItems = fruitResolver.resolve(orderCommand.orderItemsCommand());

        return orderRepository.save(new Order(orderCommand.clientName(), orderCommand.dataOrder(), orderItems));
    }
}
