package cat.itacademy.fruitapimysql.dto.fruit;

import cat.itacademy.fruitapimysql.dto.supplier.SupplierDtoRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FruitDtoRequest(
        Long id,

        @NotBlank(message = "name cannot be empty")
        String name,

        @NotNull( message = "Weight cannot be empty")
        @Positive(message = "Weight must be positive")
        double weightKg,

        @NotNull (message = "Supplier cannot be empty")
        SupplierDtoRequest supplierDtoRequest
) {}