package cat.itacademy.fruitorderapimongo.application.mapper;

import cat.itacademy.fruitorderapimongo.domain.model.valueobject.DataOrder;
import cat.itacademy.fruitorderapimongo.domain.model.order.Order;
import cat.itacademy.fruitorderapimongo.domain.model.order.OrderItem;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.Name;
import cat.itacademy.fruitorderapimongo.application.dto.order.OrderDtoRequest;
import cat.itacademy.fruitorderapimongo.application.dto.order.OrderDtoResponse;

import java.util.List;

public class OrderMapper {
    public static Order toEntity(OrderDtoRequest orderDtoRequest, List<OrderItem> orderItems) {
        return new Order(Name.of(orderDtoRequest.clientName()), DataOrder.of(orderDtoRequest.deliveryDate()), orderItems);
    }

    public static OrderDtoResponse toDto(Order order) {
        return new OrderDtoResponse(
                order.getId(),
                order.getClientName().getValue(),
                order.getDataOrder().getDeliveryDate(),
                order.getOrderItems().stream().map(OrderItemMapper::toDto).toList()
        );
    }

}
