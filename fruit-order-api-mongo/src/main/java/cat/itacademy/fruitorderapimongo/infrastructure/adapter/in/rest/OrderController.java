package cat.itacademy.fruitorderapimongo.infrastructure.adapter.in.rest;

import cat.itacademy.fruitorderapimongo.domain.model.order.Order;
import cat.itacademy.fruitorderapimongo.domain.usecase.order.CreateOrderUseCase;
import cat.itacademy.fruitorderapimongo.domain.usecase.order.GetAllOrdersUseCase;
import cat.itacademy.fruitorderapimongo.domain.usecase.order.GetOrderByIdUseCase;
import cat.itacademy.fruitorderapimongo.infrastructure.dto.order.OrderDtoRequest;
import cat.itacademy.fruitorderapimongo.infrastructure.dto.order.OrderDtoResponse;
import cat.itacademy.fruitorderapimongo.infrastructure.mapper.OrderMapper;
import cat.itacademy.fruitorderapimongo.domain.usecase.order.CreateOrderUseCase.OrderItemInput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping ("/orders")
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;

    public OrderController(CreateOrderUseCase createOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
    }

    @PostMapping
    ResponseEntity<OrderDtoResponse> createOrder(@RequestBody OrderDtoRequest order){
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
}
