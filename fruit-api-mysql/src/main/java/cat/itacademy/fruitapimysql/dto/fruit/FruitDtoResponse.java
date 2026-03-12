package cat.itacademy.fruitapimysql.dto.fruit;

import cat.itacademy.fruitapimysql.dto.supplier.SupplierDtoResponse;

public record FruitDtoResponse (Long id, String name, Double weightKg, SupplierDtoResponse supplierDtoResponse) {
}
