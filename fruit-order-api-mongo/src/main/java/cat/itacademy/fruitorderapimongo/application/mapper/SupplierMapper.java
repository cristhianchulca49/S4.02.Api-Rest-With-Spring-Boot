package cat.itacademy.fruitorderapimongo.application.mapper;

import cat.itacademy.fruitorderapimongo.application.dto.supplier.SupplierDtoRequest;
import cat.itacademy.fruitorderapimongo.application.dto.supplier.SupplierDtoResponse;
import cat.itacademy.fruitorderapimongo.domain.model.Supplier;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.Country;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.Name;


public class SupplierMapper {
    public static Supplier toEntity(SupplierDtoRequest supplierDtoRequest) {
        return new Supplier(Name.of(supplierDtoRequest.name()), Country.of(supplierDtoRequest.country()));
    }

    public static SupplierDtoResponse toDto(Supplier supplier) {
        return new SupplierDtoResponse(
                supplier.getId(),
                supplier.getName().getValue(),
                supplier.getCountry().getValue()
        );
    }
}
