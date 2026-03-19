package cat.itacademy.fruitorderapimongo.dto.fruit;

import cat.itacademy.fruitorderapimongo.dto.supplier.SupplierDtoResponse;

public record FruitDtoResponse (String id, String name, Double weightKg, SupplierDtoResponse supplierDtoResponse) {
}
