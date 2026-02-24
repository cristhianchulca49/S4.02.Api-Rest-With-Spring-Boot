package cat.itacademy.fruitapimysql.mapper;

import cat.itacademy.fruitapimysql.dto.FruitDto;
import cat.itacademy.fruitapimysql.model.Fruit;

public class FruitMapper {
    public static Fruit toEntity(FruitDto fruitDto) {
        return new Fruit(fruitDto.name(), fruitDto.weightKg(), SupplierMapper.toEntity(fruitDto.supplierDto()));
    }

    public static FruitDto toDto(Fruit fruit) {
        return new FruitDto(fruit.getId(), fruit.getName(), fruit.getWeightKg(), SupplierMapper.toDto(fruit.getSupplier()));
    }
}
