package cat.itacademy.fruitorderapimongo.service;

import cat.itacademy.fruitorderapimongo.domain.model.Fruit;
import cat.itacademy.fruitorderapimongo.domain.model.order.OrderItem;
import cat.itacademy.fruitorderapimongo.infrastructure.dto.order.OrderDtoRequest;
import cat.itacademy.fruitorderapimongo.infrastructure.dto.order.OrderDtoResponse;
import cat.itacademy.fruitorderapimongo.infrastructure.dto.order.OrderItemDtoRequest;
import cat.itacademy.fruitorderapimongo.infrastructure.mapper.OrderMapper;
import cat.itacademy.fruitorderapimongo.infrastructure.mapper.OrderItemMapper;
import cat.itacademy.fruitorderapimongo.infrastructure.adapter.out.persistence.FruitRepository;
import cat.itacademy.fruitorderapimongo.infrastructure.adapter.out.persistence.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final FruitRepository fruitRepository;

    public OrderService(OrderRepository orderRepository, FruitRepository fruitRepository) {
        this.orderRepository = orderRepository;
        this.fruitRepository = fruitRepository;
    }

    public OrderDtoResponse createOrder(OrderDtoRequest orderDtoRequest) {
        Set<String> idItems = orderDtoRequest.items().stream()
                .map(OrderItemDtoRequest::fruitId)
                .collect(Collectors.toSet());

        List<Fruit> fruits = fruitRepository.findAllById(idItems);

        if(idItems.size() != fruits.size()) {
            throw new IllegalArgumentException("Some fruits not found");
        }

        Map<String, Fruit> fruitsById = fruits.stream()
                .collect(Collectors.toMap(Fruit::getId, fruit -> fruit));

        List<OrderItem> orderItems = orderDtoRequest.items().stream()
                .map(orderItemDtoRequest -> OrderItemMapper.toEntity(orderItemDtoRequest, fruitsById.get(orderItemDtoRequest.fruitId())))
                .toList();

        return OrderMapper.toDto(orderRepository.save(OrderMapper.toEntity(orderDtoRequest, orderItems)));
    }
}
