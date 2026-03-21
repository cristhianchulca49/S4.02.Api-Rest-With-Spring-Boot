package cat.itacademy.fruitorderapimongo.application.usecase.order.command;

import cat.itacademy.fruitorderapimongo.domain.model.valueobject.DataOrder;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.Name;

import java.util.List;

public record OrderCommand(Name clientName, DataOrder dataOrder, List<OrderItemCommand> orderItemsCommand) {
}

