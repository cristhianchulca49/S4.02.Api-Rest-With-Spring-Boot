package cat.itacademy.fruitorderapimongo.model.order;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "orders")
@Getter
public class Order {

    @Id
    private String id;

    @Setter
    private String nameClient;

    @Setter
    private DataOrder dataOrder;

    @Setter
    private List<OrderItem> orderItems;

    public Order(String nameClient, DataOrder dataOrder, List<OrderItem> orderItems) {
        this.nameClient = nameClient;
        this.dataOrder = dataOrder;
        this.orderItems = orderItems;
    }
}
