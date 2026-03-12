package cat.itacademy.fruitapimysql.mapper;

import cat.itacademy.fruitapimysql.dto.fruit.FruitDtoRequest;
import cat.itacademy.fruitapimysql.dto.fruit.FruitDtoResponse;
import cat.itacademy.fruitapimysql.model.Fruit;

public class FruitMapper {
    public static Fruit toEntity(FruitDtoRequest fruitDtoRequest) {
        return new Fruit(fruitDtoRequest.name(), fruitDtoRequest.weightKg(), SupplierMapper.toEntity(fruitDtoRequest.supplierDtoRequest()));
    }

    public static FruitDtoResponse toDto(Fruit fruit) {
        return new FruitDtoResponse(fruit.getId(), fruit.getName(), fruit.getWeightKg(), SupplierMapper.toDto(fruit.getSupplier()));
    }
}
