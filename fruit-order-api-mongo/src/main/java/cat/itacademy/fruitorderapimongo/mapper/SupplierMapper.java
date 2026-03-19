package cat.itacademy.fruitorderapimongo.mapper;

import cat.itacademy.fruitorderapimongo.dto.supplier.SupplierDtoRequest;
import cat.itacademy.fruitorderapimongo.dto.supplier.SupplierDtoResponse;
import cat.itacademy.fruitorderapimongo.model.Supplier;


public class SupplierMapper {
    public static Supplier toEntity(SupplierDtoRequest supplierDtoRequest) {
        return new Supplier(supplierDtoRequest.name(), supplierDtoRequest.country());
    }

    public static SupplierDtoResponse toDto(Supplier supplier) {
        return new SupplierDtoResponse(supplier.getId(), supplier.getName(), supplier.getCountry());
    }
}
