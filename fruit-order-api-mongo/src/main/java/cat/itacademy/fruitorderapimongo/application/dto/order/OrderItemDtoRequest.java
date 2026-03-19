package cat.itacademy.fruitorderapimongo.application.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemDtoRequest(
        @NotBlank(message = "fruitId cannot be blank")
        String fruitId,

        @NotNull(message = "quantityInKg cannot be empty")
        @Positive(message = "quantityInKg must be positive")
        Double quantityInKg
) {
}
