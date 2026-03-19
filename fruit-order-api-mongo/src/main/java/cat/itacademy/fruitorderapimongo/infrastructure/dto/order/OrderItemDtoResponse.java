package cat.itacademy.fruitorderapimongo.infrastructure.dto.order;

import cat.itacademy.fruitorderapimongo.infrastructure.dto.fruit.FruitDtoResponse;

public record OrderItemDtoResponse(
        FruitDtoResponse fruit,
        Double quantityInKg
) {
}
