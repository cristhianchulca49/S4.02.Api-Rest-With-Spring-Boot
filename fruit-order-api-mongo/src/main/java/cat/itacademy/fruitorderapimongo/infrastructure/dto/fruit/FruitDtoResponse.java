package cat.itacademy.fruitorderapimongo.infrastructure.dto.fruit;

import cat.itacademy.fruitorderapimongo.infrastructure.dto.supplier.SupplierDtoResponse;

public record FruitDtoResponse (String id, String name, Double weightKg, SupplierDtoResponse supplierDtoResponse) {
}
