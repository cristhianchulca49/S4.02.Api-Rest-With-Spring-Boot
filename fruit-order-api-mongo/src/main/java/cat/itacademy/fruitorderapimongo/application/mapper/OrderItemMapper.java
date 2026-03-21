package cat.itacademy.fruitorderapimongo.application.mapper;

import cat.itacademy.fruitorderapimongo.application.dto.order.OrderItemDtoRequest;
import cat.itacademy.fruitorderapimongo.application.dto.order.OrderItemDtoResponse;
import cat.itacademy.fruitorderapimongo.application.usecase.order.command.OrderItemCommand;
import cat.itacademy.fruitorderapimongo.domain.model.Fruit;
import cat.itacademy.fruitorderapimongo.domain.model.order.OrderItem;

import java.util.List;

public class OrderItemMapper {

    public static List<OrderItemCommand> toCommand(List<OrderItemDtoRequest> orderItemDtoRequest) {
        return orderItemDtoRequest.stream()
                .map(item -> new OrderItemCommand(item.fruitId(), item.quantityInKg()))
                .toList();
    }

    public static OrderItemDtoResponse toDto(OrderItem orderItem) {
        return  new OrderItemDtoResponse(FruitMapper.toDto(orderItem.getFruit()), orderItem.getQuantityInKg());
    }

}
