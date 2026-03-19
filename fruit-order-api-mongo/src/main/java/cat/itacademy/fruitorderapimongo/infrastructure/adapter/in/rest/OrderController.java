package cat.itacademy.fruitorderapimongo.infrastructure.adapter.in.rest;

import cat.itacademy.fruitorderapimongo.application.usecase.order.GetAllOrdersUseCase;
import cat.itacademy.fruitorderapimongo.application.usecase.order.GetOrderByIdUseCase;
import cat.itacademy.fruitorderapimongo.domain.model.order.Order;
import cat.itacademy.fruitorderapimongo.application.usecase.order.CreateOrderUseCase;
import cat.itacademy.fruitorderapimongo.application.dto.order.OrderDtoRequest;
import cat.itacademy.fruitorderapimongo.application.dto.order.OrderDtoResponse;
import cat.itacademy.fruitorderapimongo.application.mapper.OrderMapper;
import cat.itacademy.fruitorderapimongo.application.usecase.order.CreateOrderUseCase.OrderItemInput;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping ("/orders")
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
    private final GetAllOrdersUseCase getAllOrdersUseCase;
    private final GetOrderByIdUseCase getOrderByIdUseCase;

    public OrderController(CreateOrderUseCase createOrderUseCase, GetAllOrdersUseCase getAllOrdersUseCase, GetOrderByIdUseCase getOrderByIdUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.getAllOrdersUseCase = getAllOrdersUseCase;
        this.getOrderByIdUseCase = getOrderByIdUseCase;
    }

    @PostMapping
    ResponseEntity<OrderDtoResponse> createOrder(@RequestBody @Valid OrderDtoRequest order){
        Order createdOrder = createOrderUseCase.execute(
                order.clientName(),
                order.deliveryDate(),
                order.items().stream()
                        .map(item -> new OrderItemInput(item.fruitId(), item.quantityInKg()))
                        .toList()
        );
        OrderDtoResponse response = OrderMapper.toDto(createdOrder);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    ResponseEntity<List<OrderDtoResponse>> getAllOrders() {
        List<Order> orders = getAllOrdersUseCase.execute();
        List<OrderDtoResponse> response = orders.stream()
                .map(OrderMapper::toDto)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    ResponseEntity<OrderDtoResponse> getOrderById(@PathVariable String id){
        Order order = getOrderByIdUseCase.execute(id);
        OrderDtoResponse response = OrderMapper.toDto(order);
        return ResponseEntity.ok(response);
    }
}
