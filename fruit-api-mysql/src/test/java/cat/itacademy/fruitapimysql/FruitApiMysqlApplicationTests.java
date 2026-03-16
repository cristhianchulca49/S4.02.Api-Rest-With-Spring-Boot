package cat.itacademy.fruitapimysql;

import cat.itacademy.fruitapimysql.dto.fruit.FruitDtoRequest;
import cat.itacademy.fruitapimysql.dto.supplier.SupplierDtoRequest;
import cat.itacademy.fruitapimysql.repository.FruitRepository;
import cat.itacademy.fruitapimysql.repository.SupplierRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FruitApiMysqlApplicationTests {

    @LocalServerPort
    private int randomPort;

    @Autowired
    private FruitRepository fruitRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = randomPort;
        fruitRepository.deleteAll();
        supplierRepository.deleteAll();
    }

    @Test
    void shouldCreateSupplierAndReturn201() {
        SupplierDtoRequest supplierDtoRequest = new SupplierDtoRequest("Carrefour", "Spain");
        given()
                .contentType(ContentType.JSON)
                .body(supplierDtoRequest)
                .when()
                .post("/suppliers")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", is(supplierDtoRequest.name()))
                .body("country", is(supplierDtoRequest.country()));
    }

    @Test
    void create_shouldReturn409WhenSupplierNameAlreadyExists() {
        SupplierDtoRequest supplierDtoRequest = new SupplierDtoRequest("Carrefour", "Spain");
        SupplierDtoRequest supplierDuplicated = new SupplierDtoRequest("Carrefour", "Equator");

        given()
                .contentType(ContentType.JSON)
                .body(supplierDtoRequest)
                .post("/suppliers");

        given()
                .contentType(ContentType.JSON)
                .body(supplierDuplicated)
                .when()
                .post("/suppliers")
                .then()
                .statusCode(409)
                .body("message", containsStringIgnoringCase("Supplier with name Carrefour already exists"));
    }

    @Test
    void create_shouldReturn400WhenNameIsBlank() {
        SupplierDtoRequest supplierDtoRequest = new SupplierDtoRequest("", "Spain"); // ✅ nombre vacío

        given()
                .contentType(ContentType.JSON)
                .body(supplierDtoRequest)
                .when()
                .post("/suppliers")
                .then()
                .statusCode(400)
                .body("errors.name", containsStringIgnoringCase("name cannot be in blank"));
    }

    @Test
    @DisplayName("Create fruit with existing supplier should return 201")
    void createFruit_shouldReturn201() {
        SupplierDtoRequest supplierDtoRequest = new SupplierDtoRequest("Carrefour", "Spain");
        Long supplierId = given()
                .contentType(ContentType.JSON)
                .body(supplierDtoRequest)
                .post("/suppliers")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("id");

        FruitDtoRequest fruitDtoRequest = new FruitDtoRequest("Watermelon", 3.5, supplierId);

        given()
                .contentType(ContentType.JSON)
                .body(fruitDtoRequest)
                .when()
                .post("/fruits")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", is(fruitDtoRequest.name()))
                .body("weightKg", is((float) fruitDtoRequest.weightKg()))
                .body("supplierDtoResponse.id", is(supplierId.intValue()))
                .body("supplierDtoResponse.name", is(supplierDtoRequest.name()))
                .body("supplierDtoResponse.country", is(supplierDtoRequest.country()));
    }
}