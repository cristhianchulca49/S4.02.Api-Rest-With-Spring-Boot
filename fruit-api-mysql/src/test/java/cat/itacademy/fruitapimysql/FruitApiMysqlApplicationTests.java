package cat.itacademy.fruitapimysql;

import cat.itacademy.fruitapimysql.dto.SupplierDto;
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
        SupplierDto supplierDto = new SupplierDto(null, "Carrefour", "Spain");
        given()
                .contentType(ContentType.JSON)
                .body(supplierDto)
                .when()
                .post("/suppliers")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", is(supplierDto.name()))
                .body("city", is(supplierDto.city()));
    }

    @Test
    void create_shouldReturn409WhenSupplierNameAlreadyExists() {
        SupplierDto supplierDto = new SupplierDto(null, "Carrefour", "Spain");
        SupplierDto supplierDuplicated = new SupplierDto(null, "Carrefour", "Equator");

        given()
                .contentType(ContentType.JSON)
                .body(supplierDto)
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
}
