package cat.itacademy.fruitapimysql.dto;


import jakarta.validation.constraints.NotBlank;

public record SupplierDto(
        Long id,

        @NotBlank(message = "name cannot be in blank")
        String name,

        @NotBlank(message = "city cannot be in blank")
        String city
        ) {
}

