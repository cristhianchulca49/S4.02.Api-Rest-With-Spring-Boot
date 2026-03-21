package cat.itacademy.fruitorderapimongo;

import cat.itacademy.fruitorderapimongo.application.dto.fruit.FruitDtoRequest;
import cat.itacademy.fruitorderapimongo.application.dto.supplier.SupplierDtoRequest;
import cat.itacademy.fruitorderapimongo.infrastructure.adapter.out.persistence.FruitRepository;
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
        given()
                .contentType(ContentType.JSON)
                .body(new SupplierDtoRequest("Carrefour", "Spain"))
                .post("/suppliers");

        given()
                .contentType(ContentType.JSON)
                .body(new SupplierDtoRequest("Carrefour", "Equator"))
                .when()
                .post("/suppliers")
                .then()
                .statusCode(409)
                .body("message", containsStringIgnoringCase("Supplier with name Carrefour already exists"));
    }

    @Test
    @DisplayName("Create supplier with blank name should return 400")
    void create_shouldReturn400WhenNameIsBlank() {
        given()
                .contentType(ContentType.JSON)
                .body(new SupplierDtoRequest("", "Spain"))
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

        String supplierId = given()
                .contentType(ContentType.JSON)
                .body(new SupplierDtoRequest("Carrefour", "Spain"))
                .post("/suppliers")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");

        given()
                .contentType(ContentType.JSON)
                .body(new SupplierDtoRequest("Mercadona", "Germany"))
                .when()
                .put("/suppliers/{id}", supplierId)
                .then()
                .statusCode(200)
                .body("id", is(supplierId))
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
                .put("/suppliers/{id}", "nonExistingId")
                .then()
                .statusCode(404)
                .body("message", containsStringIgnoringCase("Supplier with id: nonExistingId not found"));
    }

    @Test
    @DisplayName("Update supplier with blank name should return 400")
    void updateSupplier_shouldReturn400() {
        given()
                .contentType(ContentType.JSON)
                .body(new SupplierDtoRequest("", "Spain"))
                .when()
                .put("/suppliers/{id}", "nonExistingId")
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

        String supplierId2 = given()
                .contentType(ContentType.JSON)
                .body(new SupplierDtoRequest("Mercadona", "Spain"))
                .post("/suppliers")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");  // 👈 String

        given()
                .contentType(ContentType.JSON)
                .body(new SupplierDtoRequest("Carrefour", "Spain"))
                .when()
                .put("/suppliers/{id}", supplierId2)
                .then()
                .statusCode(409)
                .body("message", containsStringIgnoringCase("Supplier with name Carrefour already exists"));
    }

    // DELETE SUPPLIER
    @Test
    @DisplayName("Delete supplier should return 204")
    void deleteSupplier_shouldReturn204() {
        String supplierId = given()
                .contentType(ContentType.JSON)
                .body(new SupplierDtoRequest("Carrefour", "Spain"))
                .post("/suppliers")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");  // 👈 String

        given()
                .when()
                .delete("/suppliers/{id}", supplierId)
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("Delete non existing supplier should return 404")
    void deleteSupplier_shouldReturn404() {
        given()
                .when()
                .delete("/suppliers/{id}", "nonExistingId")
                .then()
                .statusCode(404)
                .body("message", containsStringIgnoringCase("Supplier with id: nonExistingId not found"));
    }

    @Test
    @DisplayName("Delete supplier with fruits associated should return 400")
    void deleteSupplier_shouldReturn400() {
        String supplierId = given()
                .contentType(ContentType.JSON)
                .body(new SupplierDtoRequest("Carrefour", "Spain"))
                .post("/suppliers")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");

        given()
                .contentType(ContentType.JSON)
                .body(new FruitDtoRequest("Pineapple", 3.4, supplierId))
                .post("/fruits");

        given()
                .when()
                .delete("/suppliers/{id}", supplierId)
                .then()
                .statusCode(400)
                .body("message", containsStringIgnoringCase("Supplier with id: " + supplierId + " has fruits associated"));
    }
}