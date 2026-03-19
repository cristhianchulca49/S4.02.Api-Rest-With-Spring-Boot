package cat.itacademy.fruitorderapimongo.mapper;


import cat.itacademy.fruitorderapimongo.dto.fruit.FruitDtoRequest;
import cat.itacademy.fruitorderapimongo.dto.fruit.FruitDtoResponse;
import cat.itacademy.fruitorderapimongo.model.Fruit;
import cat.itacademy.fruitorderapimongo.model.Supplier;

public class FruitMapper {
    public static Fruit toEntity(FruitDtoRequest fruitDtoRequest, Supplier supplier) {
        return new Fruit(fruitDtoRequest.name(), fruitDtoRequest.weightKg(), supplier);
    }

    public static FruitDtoResponse toDto(Fruit fruit) {
        return new FruitDtoResponse(fruit.getId(), fruit.getName(), fruit.getWeightKg(), SupplierMapper.toDto(fruit.getSupplier()));
    }
}