package cat.itacademy.fruitorderapimongo.application.dto.order;

import java.time.LocalDate;
import java.util.List;

public record OrderDtoResponse(
        String id,
        String clientName,
        LocalDate deliveryDate,
        List<OrderItemDtoResponse> orderItems
) {
}
