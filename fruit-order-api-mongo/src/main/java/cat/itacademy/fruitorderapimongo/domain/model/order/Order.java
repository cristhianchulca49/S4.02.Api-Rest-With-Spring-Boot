package cat.itacademy.fruitorderapimongo.domain.model.order;

import cat.itacademy.fruitorderapimongo.domain.model.valueobject.DataOrder;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.Name;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "orders")
@Getter
public class Order {

    @Id
    private String id;

    private Name clientName;

    private DataOrder dataOrder;

    private List<OrderItem> orderItems;

    public Order(Name clientName, DataOrder dataOrder, List<OrderItem> orderItems) {
        this.clientName = clientName;
        this.dataOrder = dataOrder;
        this.orderItems = orderItems;
    }

    public void changeName(Name clientName) {
        this.clientName = clientName;
    }

    public void changeDataOrder(DataOrder dataOrder) {
        this.dataOrder = dataOrder;
    }

    public void changeOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
