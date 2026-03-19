package cat.itacademy.fruitorderapimongo;

import cat.itacademy.fruitorderapimongo.domain.model.Fruit;
import cat.itacademy.fruitorderapimongo.domain.model.Supplier;
import cat.itacademy.fruitorderapimongo.domain.model.order.Order;
import cat.itacademy.fruitorderapimongo.domain.model.order.OrderItem;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.Country;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.DataOrder;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.Name;
import cat.itacademy.fruitorderapimongo.domain.model.valueobject.PricePerKg;
import cat.itacademy.fruitorderapimongo.application.usecase.order.CreateOrderUseCase;
import cat.itacademy.fruitorderapimongo.domain.port.out.FruitRepositoryPort;
import cat.itacademy.fruitorderapimongo.domain.port.out.OrderRepositoryPort;
import cat.itacademy.fruitorderapimongo.infrastructure.adapter.in.rest.OrderController;
import cat.itacademy.fruitorderapimongo.application.dto.order.OrderDtoRequest;
import cat.itacademy.fruitorderapimongo.application.dto.order.OrderItemDtoRequest;
import cat.itacademy.fruitorderapimongo.domain.exception.GlobalExceptionHandler;
import cat.itacademy.fruitorderapimongo.domain.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@Import({GlobalExceptionHandler.class, OrderTest.TestConfig.class})
class OrderTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestCreateOrderUseCase createOrderUseCase;

    @Test
    void createOrder_shouldBe201() throws Exception {
        LocalDate deliveryDate = LocalDate.now().plusDays(1);
        OrderDtoRequest orderDtoRequest = new OrderDtoRequest(
                "John",
                deliveryDate,
                List.of(new OrderItemDtoRequest("fruit-1", 1.7))
        );

        Order createdOrder = buildOrder("order-1", "John", deliveryDate, "fruit-1", "Apple", 4.5, "supplier-1", "Carrefour", "Spain", 1.7);
        createOrderUseCase.setResponse(createdOrder);
        createOrderUseCase.setException(null);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDtoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("order-1"))
                .andExpect(jsonPath("$.clientName").value("John"))
                .andExpect(jsonPath("$.orderItems[0].quantityInKg").value(1.7));
    }

    @Test
    void createOrder_shouldBe404WhenFruitNotFound() throws Exception {
        LocalDate deliveryDate = LocalDate.now().plusDays(1);
        OrderDtoRequest orderDtoRequest = new OrderDtoRequest(
                "John",
                deliveryDate,
                List.of(new OrderItemDtoRequest("fruit-1", 1.7))
        );

        createOrderUseCase.setResponse(null);
        createOrderUseCase.setException(new ResourceNotFoundException("Fruit", "fruit-1"));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDtoRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Fruit with id: fruit-1 not found"));
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        TestCreateOrderUseCase createOrderUseCase() {
            return new TestCreateOrderUseCase();
        }
    }

    static class TestCreateOrderUseCase extends CreateOrderUseCase {
        private volatile Order response;
        private volatile RuntimeException exception;

        TestCreateOrderUseCase() {
            super(new OrderRepositoryPort() {
                @Override
                public Order save(Order order) {
                    return order;
                }

                @Override
                public java.util.Optional<Order> findById(String id) {
                    return java.util.Optional.empty();
                }

                @Override
                public java.util.List<Order> findAll() {
                    return java.util.List.of();
                }
            }, new FruitRepositoryPort() {
                @Override
                public Fruit save(Fruit fruit) {
                    return fruit;
                }

                @Override
                public java.util.Optional<Fruit> findById(String id) {
                    return java.util.Optional.empty();
                }

                @Override
                public java.util.List<Fruit> findAll() {
                    return java.util.List.of();
                }

                @Override
                public boolean existsByName(String name) {
                    return false;
                }

                @Override
                public void delete(Fruit fruit) {
                }

                @Override
                public java.util.List<Fruit> findBySupplierId(String supplierId) {
                    return java.util.List.of();
                }
            });
        }

        @Override
        public Order execute(String clientName, LocalDate deliveryDate, java.util.List<CreateOrderUseCase.OrderItemInput> items) {
            if (exception != null) {
                throw exception;
            }
            return response;
        }

        void setResponse(Order response) {
            this.response = response;
        }

        void setException(RuntimeException exception) {
            this.exception = exception;
        }
    }

    private static Order buildOrder(String orderId,
                                    String clientName,
                                    LocalDate deliveryDate,
                                    String fruitId,
                                    String fruitName,
                                    Double pricePerKg,
                                    String supplierId,
                                    String supplierName,
                                    String supplierCountry,
                                    Double quantityInKg) throws ReflectiveOperationException {
        Supplier supplier = new Supplier(Name.of(supplierName), Country.of(supplierCountry));
        setField(supplier, "id", supplierId);

        Fruit fruit = new Fruit(Name.of(fruitName), PricePerKg.of(pricePerKg), supplier);
        setField(fruit, "id", fruitId);

        Order order = new Order(Name.of(clientName), DataOrder.of(deliveryDate), List.of(new OrderItem(fruit, quantityInKg)));
        setField(order, "id", orderId);
        return order;
    }

    private static void setField(Object target, String fieldName, Object value) throws ReflectiveOperationException {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
