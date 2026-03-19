package cat.itacademy.fruitorderapimongo.application.mapper;


import cat.itacademy.fruitorderapimongo.application.dto.fruit.FruitDtoRequest;
import cat.itacademy.fruitorderapimongo.application.dto.fruit.FruitDtoResponse;
import cat.itacademy.fruitorderapimongo.domain.model.Fruit;
import cat.itacademy.fruitorderapimongo.domain.model.Supplier;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.Name;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.PricePerKg;

public class FruitMapper {
    public static Fruit toEntity(FruitDtoRequest fruitDtoRequest, Supplier supplier) {
        return new Fruit(Name.of(fruitDtoRequest.name()), PricePerKg.of(fruitDtoRequest.pricePerKg()), supplier);
    }

    public static FruitDtoResponse toDto(Fruit fruit) {
        return new FruitDtoResponse(
                fruit.getId(),
                fruit.getName().getValue(),
                fruit.getPrice().getValue(),
                SupplierMapper.toDto(fruit.getSupplier())
        );
    }
}
