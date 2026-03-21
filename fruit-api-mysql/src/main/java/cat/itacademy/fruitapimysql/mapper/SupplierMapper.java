package cat.itacademy.fruitapimysql.mapper;

import cat.itacademy.fruitapimysql.dto.supplier.SupplierDtoRequest;
import cat.itacademy.fruitapimysql.dto.supplier.SupplierDtoResponse;
import cat.itacademy.fruitapimysql.model.Supplier;

public class SupplierMapper {
    public static Supplier toEntity(SupplierDtoRequest supplierDtoRequest) {
        return new Supplier(supplierDtoRequest.name(), supplierDtoRequest.country());
    }

    public static SupplierDtoResponse toDto(Supplier supplier) {
        return new SupplierDtoResponse(supplier.getId(), supplier.getName(), supplier.getCountry());
    }
}
