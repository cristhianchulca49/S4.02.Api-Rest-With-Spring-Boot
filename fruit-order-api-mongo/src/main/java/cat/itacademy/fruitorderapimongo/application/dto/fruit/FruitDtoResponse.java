package cat.itacademy.fruitorderapimongo.application.dto.fruit;

import cat.itacademy.fruitorderapimongo.application.dto.supplier.SupplierDtoResponse;

public record FruitDtoResponse (String id, String name, Double pricePerKg, SupplierDtoResponse supplierDtoResponse) {
}
