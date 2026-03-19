package cat.itacademy.fruitorderapimongo.infrastructure.dto.supplier;


import jakarta.validation.constraints.NotBlank;

public record SupplierDtoRequest(

        @NotBlank(message = "name cannot be in blank")
        String name,

        @NotBlank(message = "country cannot be in blank")
        String country
        ) {
}

