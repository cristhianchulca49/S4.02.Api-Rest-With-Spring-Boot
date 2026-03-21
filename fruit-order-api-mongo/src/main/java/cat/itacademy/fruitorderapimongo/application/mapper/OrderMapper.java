package cat.itacademy.fruitorderapimongo.application.mapper;

import cat.itacademy.fruitorderapimongo.application.usecase.order.command.OrderCommand;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.DataOrder;
import cat.itacademy.fruitorderapimongo.domain.model.order.Order;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.Name;
import cat.itacademy.fruitorderapimongo.application.dto.order.OrderDtoRequest;
import cat.itacademy.fruitorderapimongo.application.dto.order.OrderDtoResponse;

import java.util.List;

public class OrderMapper {
    public static OrderCommand toCommand(OrderDtoRequest orderDtoRequest) {
        return new OrderCommand(Name.of(orderDtoRequest.clientName()), DataOrder.of(orderDtoRequest.deliveryDate()), OrderItemMapper.toCommand(orderDtoRequest.items()));
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
