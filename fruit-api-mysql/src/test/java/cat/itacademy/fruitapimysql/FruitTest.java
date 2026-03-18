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

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FruitTest {

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

    // ── FRUIT ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Create fruit with existing supplier should return 201")
    void createFruit_shouldReturn201() {
        SupplierDtoRequest supplierDtoRequest = new SupplierDtoRequest("Carrefour", "Spain");
        Long supplierId = given()
                .contentType(ContentType.JSON)
                .body(supplierDtoRequest)
                .post("/suppliers")
                .then()
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

    @Test
    @DisplayName("Create fruit with non existing supplier should return 404")
    void createFruit_shouldReturn404() {
        FruitDtoRequest fruitDtoRequest = new FruitDtoRequest("Watermelon", 3.5, 999L); // 👈 999L deja claro que no existe

        given()
                .contentType(ContentType.JSON)
                .body(fruitDtoRequest)
                .when()
                .post("/fruits")
                .then()
                .statusCode(404)
                .body("message", containsStringIgnoringCase("Supplier with id: 999 not found"));
    }

    @Test
    @DisplayName("Create fruit with null supplierId should return 400")
    void createFruit_shouldReturn400WhenSupplierIdIsNull() {
        FruitDtoRequest fruitDtoRequest = new FruitDtoRequest("Watermelon", 3.5, null);

        given()
                .contentType(ContentType.JSON)
                .body(fruitDtoRequest)
                .when()
                .post("/fruits")
                .then()
                .statusCode(400)
                .body("errors.supplierId", containsStringIgnoringCase("Supplier id cannot be empty"));
    }

    //GET FRUITS

    @Test
    @DisplayName("Get fruits by supplier and should return 200")
    void getFruitsBySupplier() {
        SupplierDtoRequest supplierDtoRequest = new SupplierDtoRequest("Carrefour", "Spain");
        Long supplierId = given()
                .contentType(ContentType.JSON)
                .body(supplierDtoRequest)
                .post("/suppliers")
                .then()
                .extract()
                .jsonPath()
                .getLong("id");

        List<FruitDtoRequest> fruits = List.of(new FruitDtoRequest("Watermelon", 3.3, supplierId),
                new FruitDtoRequest("Banana", 34.3, supplierId));

        fruits.forEach(fruit ->
                given()
                        .contentType(ContentType.JSON)
                        .body(fruit)
                        .post("/fruits"));

        given()
                .when()
                .get("/fruits?supplierId={id}", supplierId)
                .then()
                .statusCode(200)
                .body("$", hasSize(fruits.size()))
                .body("name", hasItems("Watermelon", "Banana"));
    }

    @Test
    @DisplayName("Get Fruits by non existing supplier should return 404")
    void getFruitsBySupplier_shouldReturn404() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/fruits?supplierId=999")
                .then()
                .statusCode(404)
                .body("message", containsStringIgnoringCase("Supplier with id: 999 not found"));
    }
}