package cat.itacademy.fruitorderapimongo;

import cat.itacademy.fruitorderapimongo.application.dto.fruit.FruitDtoRequest;
import cat.itacademy.fruitorderapimongo.application.dto.order.OrderDtoRequest;
import cat.itacademy.fruitorderapimongo.application.dto.order.OrderItemDtoRequest;
import cat.itacademy.fruitorderapimongo.application.dto.supplier.SupplierDtoRequest;
import cat.itacademy.fruitorderapimongo.infrastructure.adapter.out.persistence.FruitRepository;
import cat.itacademy.fruitorderapimongo.infrastructure.adapter.out.persistence.OrderRepository;
import cat.itacademy.fruitorderapimongo.infrastructure.adapter.out.persistence.SupplierRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderIntegrationTest {

    @LocalServerPort
    private int randomPort;

    @Autowired
    private FruitRepository fruitRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = randomPort;
        fruitRepository.deleteAll();
        supplierRepository.deleteAll();
        orderRepository.deleteAll();
    }

    // CREATE ORDER
    @Test
    @DisplayName("Create order with valid supplier, fruit and items should return 201")
    void createOrder_shouldReturn201() {

        String supplierId = given()
                .contentType(ContentType.JSON)
                .body(new SupplierDtoRequest("Carrefour", "Spain"))
                .post("/suppliers")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");

        String fruitId = given()
                .contentType(ContentType.JSON)
                .body(new FruitDtoRequest("Watermelon", 2.5, supplierId))
                .post("/fruits")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");

        OrderDtoRequest orderRequest = new OrderDtoRequest(
                "John Doe",
                LocalDate.now().plusDays(1),
                List.of(new OrderItemDtoRequest(fruitId, 3.5))
        );

        given()
                .contentType(ContentType.JSON)
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("clientName", is("John Doe"))
                .body("deliveryDate", is(LocalDate.now().plusDays(1).toString()))
                .body("orderItems", hasSize(1))
                .body("orderItems[0].quantityInKg", is(3.5f))
                .body("orderItems[0].fruit.id", is(fruitId))
                .body("orderItems[0].fruit.name", is("Watermelon"));
    }

    @Test
    @DisplayName("Create order with non existing fruit should return 404")
    void createOrder_shouldReturn404WhenFruitNotFound() {
        OrderDtoRequest orderRequest = new OrderDtoRequest(
                "John Doe",
                LocalDate.now().plusDays(1),
                List.of(new OrderItemDtoRequest("nonExistingFruitId", 3.5))
        );

        given()
                .contentType(ContentType.JSON)
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .statusCode(404)
                .body("message", containsStringIgnoringCase("Fruit with id: nonExistingFruitId not found"));
    }

    @Test
    @DisplayName("Create order with past delivery date should return 400")
    void createOrder_shouldReturn400WhenDateIsInPast() {
        OrderDtoRequest orderRequest = new OrderDtoRequest(
                "John Doe",
                LocalDate.now().minusDays(1),
                List.of(new OrderItemDtoRequest("anyFruitId", 3.5))
        );

        given()
                .contentType(ContentType.JSON)
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Create order with empty items should return 400")
    void createOrder_shouldReturn400WhenItemsAreEmpty() {
        OrderDtoRequest orderRequest = new OrderDtoRequest(
                "John Doe",
                LocalDate.now().plusDays(1),
                List.of()
        );

        given()
                .contentType(ContentType.JSON)
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .statusCode(400)
                .body("errors.items", containsStringIgnoringCase("items cannot be empty"));
    }

    @Test
    @DisplayName("Create order with blank client name should return 400")
    void createOrder_shouldReturn400WhenClientNameIsBlank() {
        OrderDtoRequest orderRequest = new OrderDtoRequest(
                "",
                LocalDate.now().plusDays(1),
                List.of(new OrderItemDtoRequest("anyFruitId", 3.5))
        );

        given()
                .contentType(ContentType.JSON)
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .statusCode(400)
                .body("errors.clientName", containsStringIgnoringCase("client name cannot be blank"));
    }
}