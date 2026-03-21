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
        orderRepository.deleteAll();
        fruitRepository.deleteAll();
        supplierRepository.deleteAll();
    }

    // ── CREATE ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Create order with valid supplier, fruit and items should return 201")
    void createOrder_shouldReturn201() {
        String supplierId = createSupplier("Carrefour", "Spain");
        String fruitId = createFruit("Watermelon", 2.5, supplierId);

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
                .body("deliveryDate", is(orderRequest.deliveryDate().toString()))
                .body("orderItems", hasSize(1))
                .body("orderItems[0].quantityInKg", is(3.5f))
                .body("orderItems[0].fruit.id", is(fruitId))
                .body("orderItems[0].fruit.name", is("Watermelon"));
    }

    @Test
    @DisplayName("Create order with non existing fruit should return 404")
    void createOrder_shouldReturn404WhenFruitNotFound() {
        given()
                .contentType(ContentType.JSON)
                .body(new OrderDtoRequest(
                        "John Doe",
                        LocalDate.now().plusDays(1),
                        List.of(new OrderItemDtoRequest("nonExistingFruitId", 3.5))))
                .when()
                .post("/orders")
                .then()
                .statusCode(404)
                .body("message", containsStringIgnoringCase("Fruit with id: nonExistingFruitId not found"));
    }

    @Test
    @DisplayName("Create order with past delivery date should return 400")
    void createOrder_shouldReturn400WhenDateIsInPast() {
        given()
                .contentType(ContentType.JSON)
                .body(new OrderDtoRequest(
                        "John Doe",
                        LocalDate.now().minusDays(1),
                        List.of(new OrderItemDtoRequest("anyFruitId", 3.5))))
                .when()
                .post("/orders")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Create order with empty items should return 400")
    void createOrder_shouldReturn400WhenItemsAreEmpty() {
        given()
                .contentType(ContentType.JSON)
                .body(new OrderDtoRequest(
                        "John Doe",
                        LocalDate.now().plusDays(1),
                        List.of()))
                .when()
                .post("/orders")
                .then()
                .statusCode(400)
                .body("errors.items", containsStringIgnoringCase("items cannot be empty"));
    }

    @Test
    @DisplayName("Create order with blank client name should return 400")
    void createOrder_shouldReturn400WhenClientNameIsBlank() {
        given()
                .contentType(ContentType.JSON)
                .body(new OrderDtoRequest(
                        "",
                        LocalDate.now().plusDays(1),
                        List.of(new OrderItemDtoRequest("anyFruitId", 3.5))))
                .when()
                .post("/orders")
                .then()
                .statusCode(400)
                .body("errors.clientName", containsStringIgnoringCase("client name cannot be blank"));
    }

    // ── GET ALL ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Get all orders should return 200")
    void getOrders_shouldReturn200() {
        String supplierId = createSupplier("Carrefour", "Spain");
        String fruitId = createFruit("Watermelon", 2.5, supplierId);
        String orderId = createOrder("John Doe", LocalDate.now().plusDays(1),
                List.of(new OrderItemDtoRequest(fruitId, 3.5)));

        given()
                .when()
                .get("/orders")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].id", is(orderId))
                .body("[0].clientName", is("John Doe"))
                .body("[0].deliveryDate", is(LocalDate.now().plusDays(1).toString()))
                .body("[0].orderItems", hasSize(1))
                .body("[0].orderItems[0].quantityInKg", is(3.5f))
                .body("[0].orderItems[0].fruit.id", is(fruitId))
                .body("[0].orderItems[0].fruit.name", is("Watermelon"));
    }

    @Test
    @DisplayName("Get all orders when empty should return 200 and empty list")
    void getAllOrders_shouldReturn200AndEmptyList() {
        given()
                .when()
                .get("/orders")
                .then()
                .statusCode(200)
                .body("$", hasSize(0));
    }

    // ── GET BY ID ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Get order by id should return 200")
    void getOrderById_shouldReturn200() {
        String supplierId = createSupplier("Carrefour", "Spain");
        String fruitId = createFruit("Watermelon", 2.5, supplierId);
        String orderId = createOrder("John Doe", LocalDate.now().plusDays(1),
                List.of(new OrderItemDtoRequest(fruitId, 3.5)));

        given()
                .when()
                .get("/orders/{id}", orderId)
                .then()
                .statusCode(200)
                .body("id", is(orderId))
                .body("clientName", is("John Doe"))
                .body("deliveryDate", is(LocalDate.now().plusDays(1).toString()))
                .body("orderItems", hasSize(1))
                .body("orderItems[0].quantityInKg", is(3.5f))
                .body("orderItems[0].fruit.id", is(fruitId))
                .body("orderItems[0].fruit.name", is("Watermelon"));
    }

    @Test
    @DisplayName("Get order by non existing id should return 404")
    void getOrderById_shouldReturn404() {
        given()
                .when()
                .get("/orders/{id}", "nonexistent-id")
                .then()
                .statusCode(404)
                .body("message", containsStringIgnoringCase("Order with id: nonexistent-id not found"));
    }

    // ── UPDATE ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Update order by id should return 200")
    void updateOrderById_shouldReturn200() {
        String supplierId = createSupplier("Carrefour", "Spain");
        String fruitId = createFruit("Watermelon", 2.5, supplierId);
        String orderId = createOrder("John Doe", LocalDate.now().plusDays(1),
                List.of(new OrderItemDtoRequest(fruitId, 3.5)));

        OrderDtoRequest updateRequest = new OrderDtoRequest(
                "Cristian IT Academy",
                LocalDate.now().plusDays(3),
                List.of(new OrderItemDtoRequest(fruitId, 2.4))
        );

        given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when()
                .put("/orders/{id}", orderId)
                .then()
                .statusCode(200)
                .body("id", is(orderId))
                .body("clientName", is(updateRequest.clientName()))
                .body("deliveryDate", is(updateRequest.deliveryDate().toString()))
                .body("orderItems", hasSize(1))
                .body("orderItems[0].fruit.id", is(fruitId))
                .body("orderItems[0].fruit.name", is("Watermelon"))
                .body("orderItems[0].quantityInKg", is(2.4f));
    }

    @Test
    @DisplayName("Update order with invalid fruitId should return 404")
    void updateOrderWithInvalidFruitId_shouldReturn404() {
        String supplierId = createSupplier("Carrefour", "Spain");
        String fruitId = createFruit("Watermelon", 2.5, supplierId);
        String orderId = createOrder("John Doe", LocalDate.now().plusDays(1),
                List.of(new OrderItemDtoRequest(fruitId, 3.5)));

        given()
                .contentType(ContentType.JSON)
                .body(new OrderDtoRequest("Cristian IT Academy", LocalDate.now().plusDays(3),
                        List.of(new OrderItemDtoRequest("no-existing-id", 2.4))))
                .when()
                .put("/orders/{id}", orderId)
                .then()
                .statusCode(404)
                .body("message", containsStringIgnoringCase("Fruit with id: no-existing-id not found"));
    }

    @Test
    @DisplayName("Update order with blank name should return 400")
    void updateOrderWithNameInBlank_shouldReturn400() {
        String supplierId = createSupplier("Carrefour", "Spain");
        String fruitId = createFruit("Watermelon", 2.5, supplierId);
        String orderId = createOrder("John Doe", LocalDate.now().plusDays(1),
                List.of(new OrderItemDtoRequest(fruitId, 3.5)));

        given()
                .contentType(ContentType.JSON)
                .body(new OrderDtoRequest("", LocalDate.now().plusDays(3),
                        List.of(new OrderItemDtoRequest(fruitId, 2.4))))
                .when()
                .put("/orders/{id}", orderId)
                .then()
                .statusCode(400)
                .body("errors.clientName", containsStringIgnoringCase("client name cannot be blank"));
    }

    @Test
    @DisplayName("Update non existing order should return 404")
    void updateOrder_shouldReturn404() {
        given()
                .contentType(ContentType.JSON)
                .body(new OrderDtoRequest("John Doe", LocalDate.now().plusDays(3),
                        List.of(new OrderItemDtoRequest("fruit-id", 2.4))))
                .when()
                .put("/orders/{id}", "no-existing-id")
                .then()
                .statusCode(404)
                .body("message", containsStringIgnoringCase("Order with id: no-existing-id not found"));
    }

    // ── HELPERS ──────────────────────────────────────────────────────────────

    private String createSupplier(String name, String country) {
        return given()
                .contentType(ContentType.JSON)
                .body(new SupplierDtoRequest(name, country))
                .post("/suppliers")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");
    }

    private String createFruit(String name, Double price, String supplierId) {
        return given()
                .contentType(ContentType.JSON)
                .body(new FruitDtoRequest(name, price, supplierId))
                .post("/fruits")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");
    }

    private String createOrder(String clientName, LocalDate deliveryDate, List<OrderItemDtoRequest> items) {
        return given()
                .contentType(ContentType.JSON)
                .body(new OrderDtoRequest(clientName, deliveryDate, items))
                .post("/orders")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");
    }
}