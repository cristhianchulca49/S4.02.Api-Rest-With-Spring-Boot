package cat.itacademy.fruitapimysql;

import cat.itacademy.fruitapimysql.dto.supplier.SupplierDtoRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class FruitApiMysqlApplicationTests {
    @LocalServerPort
    private int randomPort;

    @BeforeEach
    void setUp() {
        RestAssured.port = randomPort;
    }

    @Test
    void shouldCreateSupplierAndReturn201() {
        SupplierDtoRequest supplierDtoRequest = new SupplierDtoRequest(null, "Carrefour", "Spain");
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
        SupplierDtoRequest supplierDtoRequest = new SupplierDtoRequest(null, "Carrefour", "Spain");
        SupplierDtoRequest supplierDuplicated = new SupplierDtoRequest(null, "Carrefour", "Equator");

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
        SupplierDtoRequest supplierDtoRequest = new SupplierDtoRequest(null, "", "Spain");

        given()
                .contentType(ContentType.JSON)
                .body(supplierDtoRequest)
                .when()
                .post("/suppliers")
                .then()
                .statusCode(400)
                .body("errors.name", containsStringIgnoringCase("name cannot be in blank"));
    }
}
