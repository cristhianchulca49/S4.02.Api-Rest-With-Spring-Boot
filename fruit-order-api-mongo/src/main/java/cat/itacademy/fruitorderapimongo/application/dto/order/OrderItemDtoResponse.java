package cat.itacademy.fruitorderapimongo.application.dto.order;

import cat.itacademy.fruitorderapimongo.application.dto.fruit.FruitDtoResponse;

public record OrderItemDtoResponse(
        FruitDtoResponse fruit,
        Double quantityInKg
) {
}
