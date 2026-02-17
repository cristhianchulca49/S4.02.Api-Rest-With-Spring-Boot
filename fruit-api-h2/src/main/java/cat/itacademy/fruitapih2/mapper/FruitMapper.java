package cat.itacademy.fruitapih2.mapper;

import cat.itacademy.fruitapih2.dto.FruitDto;
import cat.itacademy.fruitapih2.model.Fruit;

public class FruitMapper {
    public static Fruit toEntity(FruitDto fruitDto) {
        return new Fruit(fruitDto.name(), fruitDto.weightKg());
    }

    public static FruitDto toDto(Fruit fruit) {
        return new FruitDto(fruit.getId(), fruit.getName(), fruit.getWeightKg());
    }
}
