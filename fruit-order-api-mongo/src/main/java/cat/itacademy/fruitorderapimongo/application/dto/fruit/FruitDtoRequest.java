package cat.itacademy.fruitorderapimongo.application.dto.fruit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FruitDtoRequest(

        @NotBlank(message = "name cannot be empty")
        String name,

        @NotNull( message = "Weight cannot be empty")
        @Positive(message = "Weight must be positive")
        Double pricePerKg,

        @NotNull (message = "Supplier id cannot be empty")
        String supplierId
) {}