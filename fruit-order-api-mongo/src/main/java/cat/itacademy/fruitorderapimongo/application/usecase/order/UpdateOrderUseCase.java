package cat.itacademy.fruitorderapimongo.application.usecase.order;

import cat.itacademy.fruitorderapimongo.application.usecase.order.command.OrderCommand;
import cat.itacademy.fruitorderapimongo.domain.exception.ResourceNotFoundException;
import cat.itacademy.fruitorderapimongo.domain.model.order.Order;
import cat.itacademy.fruitorderapimongo.domain.model.order.OrderItem;
import cat.itacademy.fruitorderapimongo.domain.port.out.OrderRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UpdateOrderUseCase {
    private final OrderRepositoryPort orderRepository;
    private final FruitResolver fruitResolver;

    public UpdateOrderUseCase(OrderRepositoryPort orderRepository, FruitResolver fruitResolver) {
        this.orderRepository = orderRepository;
        this.fruitResolver = fruitResolver;
    }

    public Order execute(String id, OrderCommand orderUpdate) {
        Order orderToUpdate = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));

        List<OrderItem> orderItems = fruitResolver.resolve(orderUpdate.orderItemsCommand());

        orderToUpdate.changeName(orderUpdate.clientName());
        orderToUpdate.changeDataOrder(orderUpdate.dataOrder());
        orderToUpdate.changeOrderItems(orderItems);

        return orderRepository.save(orderToUpdate);
    }
}
