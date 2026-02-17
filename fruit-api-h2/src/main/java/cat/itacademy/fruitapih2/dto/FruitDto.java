package cat.itacademy.fruitapih2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FruitDto(
        Long id,

        @NotBlank(message = "name cannot be empty")
        String name,

        @NotNull( message = "Weight cannot be empty")
        @Positive(message = "Weight must be positive")
        double weightKg
) {}