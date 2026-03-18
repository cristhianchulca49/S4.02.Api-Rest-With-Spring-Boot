package cat.itacademy.fruitapimysql;

import cat.itacademy.fruitapimysql.dto.fruit.FruitDtoRequest;
import cat.itacademy.fruitapimysql.dto.supplier.SupplierDtoRequest;
import cat.itacademy.fruitapimysql.repository.FruitRepository;
import cat.itacademy.fruitapimysql.repository.SupplierRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SupplierTest {
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

    // ── SUPPLIER ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Create supplier should return 201")
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
    @DisplayName("Create supplier with existing name should return 409")
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
    @DisplayName("Create supplier with blank name should return 400")
    void create_shouldReturn400WhenNameIsBlank() {
        SupplierDtoRequest supplierDtoRequest = new SupplierDtoRequest("", "Spain");

        given()
                .contentType(ContentType.JSON)
                .body(supplierDtoRequest)
                .when()
                .post("/suppliers")
                .then()
                .statusCode(400)
                .body("errors.name", containsStringIgnoringCase("name cannot be in blank"));
    }

    // UPDATE SUPPLIER
    @Test
    @DisplayName("Update supplier should return 200")
    void updateSupplier_shouldReturn200() {
        Long supplierId = given()
                .contentType(ContentType.JSON)
                .body(new SupplierDtoRequest("Carrefour", "Spain"))
                .post("/suppliers")
                .then()
                .extract().jsonPath().getLong("id");

        given()
                .contentType(ContentType.JSON)
                .body(new SupplierDtoRequest("Mercadona", "Germany"))
                .when()
                .put("/suppliers/{id}", supplierId)
                .then()
                .statusCode(200)
                .body("id", is(supplierId.intValue()))
                .body("name", is("Mercadona"))
                .body("country", is("Germany"));
    }

    @Test
    @DisplayName("Update supplier with non existing id should return 404")
    void updateSupplier_shouldReturn404() {
        given()
                .contentType(ContentType.JSON)
                .body(new SupplierDtoRequest("Mercadona", "Germany"))
                .when()
                .put("/suppliers/{id}", 999L)
                .then()
                .statusCode(404)
                .body("message", containsStringIgnoringCase("Supplier with id: 999 not found"));
    }

    @Test
    @DisplayName("Update supplier with blank name should return 400")
    void updateSupplier_shouldReturn400() {
        given()
                .contentType(ContentType.JSON)
                .body(new SupplierDtoRequest("", "Spain"))
                .when()
                .put("/suppliers/{id}", 999L)
                .then()
                .statusCode(400)
                .body("errors.name", containsStringIgnoringCase("name cannot be in blank"));
    }

    @Test
    @DisplayName("Update supplier with duplicate name should return 409")
    void updateSupplier_shouldReturn409() {
        given()
                .contentType(ContentType.JSON)
                .body(new SupplierDtoRequest("Carrefour", "Spain"))
                .post("/suppliers");

        Long supplierId2 = given()
                .contentType(ContentType.JSON)
                .body(new SupplierDtoRequest("Mercadona", "Spain"))
                .post("/suppliers")
                .then()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        given()
                .contentType(ContentType.JSON)
                .body(new SupplierDtoRequest("Carrefour", "Spain"))
                .when()
                .put("/suppliers/{id}", supplierId2)
                .then()
                .statusCode(409)
                .body("message", containsStringIgnoringCase("Supplier with name Carrefour already exists"));
    }

    //DELETE SUPPLIER
    @Test
    @DisplayName("Delete supplier should return 204")
    void deleteSupplier_shouldReturn204() {
        SupplierDtoRequest supplierDtoRequest = new SupplierDtoRequest("Carrefour", "Spain");
        Long supplierId = given()
                .contentType(ContentType.JSON)
                .body(supplierDtoRequest)
                .post("/suppliers")
                .then()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        given()
                .when()
                .delete("/suppliers/{id}", supplierId)
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("Delete unexisting Supplier should return 404")
    void deleteSupplier_shouldReturn404() {
        given()
                .when()
                .delete("/suppliers/{id}", 999L)
                .then()
                .statusCode(404)
                .body("message", containsStringIgnoringCase("Supplier with id: 999 not found"));
    }

    @Test
    @DisplayName("Delete when Supplier has fruits associated return 400")
    void deleteSupplier_shouldReturn400() {
        SupplierDtoRequest supplierDtoRequest = new SupplierDtoRequest("Carrefour", "Spain");

        Long supplierId = given()
                .contentType(ContentType.JSON)
                .body(supplierDtoRequest)
                .post("/suppliers")
                .then()
                .extract().jsonPath().getLong("id");

        FruitDtoRequest fruitDtoRequest = new FruitDtoRequest("Pineapple", 3.4, supplierId);

        given()
                .contentType(ContentType.JSON)
                .body(fruitDtoRequest)
                .post("/fruits");

        given()
                .when()
                .delete("/suppliers/{id}", supplierId)
                .then()
                .statusCode(400)
                .body("message", containsStringIgnoringCase("Supplier with id: " + supplierId + " has fruits associated"));
    }
}