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

    @Test
    @DisplayName("Create fruit with existing supplier should return 201")
    void createFruit_shouldReturn201() {
        SupplierDtoRequest supplierDtoRequest = new SupplierDtoRequest("Carrefour", "Spain");

        String supplierId = given()
                .contentType(ContentType.JSON)
                .body(supplierDtoRequest)
                .post("/suppliers")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");

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
                .body("pricePerKg", is(3.5f))
                .body("supplierDtoResponse.id", is(supplierId))
                .body("supplierDtoResponse.name", is(supplierDtoRequest.name()))
                .body("supplierDtoResponse.country", is(supplierDtoRequest.country()));
    }

    @Test
    @DisplayName("Create fruit with non existing supplier should return 404")
    void createFruit_shouldReturn404() {
        FruitDtoRequest fruitDtoRequest = new FruitDtoRequest("Watermelon", 3.5, "nonExistingSupplierId");

        given()
                .contentType(ContentType.JSON)
                .body(fruitDtoRequest)
                .when()
                .post("/fruits")
                .then()
                .statusCode(404)
                .body("message", containsStringIgnoringCase("Supplier with id: nonExistingSupplierId not found"));
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

    @Test
    @DisplayName("Get fruits by supplier should return 200")
    void getFruitsBySupplier() {
        String supplierId = given()
                .contentType(ContentType.JSON)
                .body(new SupplierDtoRequest("Carrefour", "Spain"))
                .post("/suppliers")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");

        List<FruitDtoRequest> fruits = List.of(
                new FruitDtoRequest("Watermelon", 3.3, supplierId),
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
    @DisplayName("Get fruits by non existing supplier should return 404")
    void getFruitsBySupplier_shouldReturn404() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/fruits?supplierId=nonExistingSupplierId")
                .then()
                .statusCode(404)
                .body("message", containsStringIgnoringCase("Supplier with id: nonExistingSupplierId not found"));
    }
}