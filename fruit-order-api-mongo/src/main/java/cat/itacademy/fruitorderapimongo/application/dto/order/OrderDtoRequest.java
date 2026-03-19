package cat.itacademy.fruitorderapimongo.application.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record OrderDtoRequest(
        @NotBlank(message = "client name cannot be blank")
        String clientName,

        @NotNull(message = "delivery date cannot be null")
        @Future(message = "delivery date must be in the future")
        LocalDate deliveryDate,
        @Valid
        @NotEmpty(message = "items cannot be empty")
        List<OrderItemDtoRequest> items
) {
}
