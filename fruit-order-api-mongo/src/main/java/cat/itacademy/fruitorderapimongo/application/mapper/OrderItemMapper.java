package cat.itacademy.fruitorderapimongo.application.mapper;

import cat.itacademy.fruitorderapimongo.application.dto.order.OrderItemDtoRequest;
import cat.itacademy.fruitorderapimongo.application.dto.order.OrderItemDtoResponse;
import cat.itacademy.fruitorderapimongo.domain.model.Fruit;
import cat.itacademy.fruitorderapimongo.domain.model.order.OrderItem;

public class OrderItemMapper {

    public static OrderItem toEntity(OrderItemDtoRequest orderItemDtoRequest, Fruit fruit) {
        return new OrderItem(fruit, orderItemDtoRequest.quantityInKg());
    }

    public static OrderItemDtoResponse toDto(OrderItem orderItem) {
        return  new OrderItemDtoResponse(FruitMapper.toDto(orderItem.getFruit()), orderItem.getQuantityInKg());
    }
}
