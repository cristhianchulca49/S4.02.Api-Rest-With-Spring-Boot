package cat.itacademy.fruitapimysql.mapper;

import cat.itacademy.fruitapimysql.dto.SupplierDto;
import cat.itacademy.fruitapimysql.model.Supplier;

public class SupplierMapper {
    public static Supplier toEntity(SupplierDto supplierDto) {
        return new Supplier(supplierDto.name(), supplierDto.city());
    }

    public static SupplierDto toDto(Supplier supplier) {
        return new SupplierDto(supplier.getId(), supplier.getName(), supplier.getCountry());
    }
}
