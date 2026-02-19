package cat.itacademy.fruitapih2;

import cat.itacademy.fruitapih2.dto.FruitDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class FruitApiH2ApplicationTests {

    @LocalServerPort
    private int randomPort;

    @BeforeEach
    void setUp() {
        RestAssured.port = randomPort;
    }

    @Test
    void shouldCreateFruitAndReturn201() {

        FruitDto newFruit = new FruitDto(null, "Mango", 1.2);

        given()
                .contentType(ContentType.JSON)
                .body(newFruit)
                .when()
                .post("/fruits")
                .then()
                .statusCode(201)
                .body("name", equalTo("Mango"))
                .header("Location", notNullValue());
    }

    @Test
    void createFruit_shouldReturn409WhenFruitAlreadyExists() {

        FruitDto fruit = new FruitDto(null, "Apple", 0.5);

        given()
                .contentType(ContentType.JSON)
                .body(fruit)
                .post("/fruits");

        given()
                .contentType(ContentType.JSON)
                .body(fruit)
                .when()
                .post("/fruits")
                .then()
                .statusCode(409);
    }

    @Test
    void createFruit_shouldReturn400WhenInvalidData() {

        FruitDto invalidFruit = new FruitDto(null, "", -5.0);

        given()
                .contentType(ContentType.JSON)
                .body(invalidFruit)
                .when()
                .post("/fruits")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldGetAllFruitsAndReturn200() {
        List<FruitDto> fruits = List.of(
                new FruitDto(null, "Apple", 0.5),
                new FruitDto(null, "Banana", 0.3),
                new FruitDto(null, "Watermelon", 5.0),
                new FruitDto(null, "Mango", 1.2),
                new FruitDto(null, "Pear", 0.4),
                new FruitDto(null, "Pineapple", 2.1)
        );

        fruits.forEach(fruit -> {
            given()
                    .contentType(ContentType.JSON)
                    .body(fruit)
                    .post("/fruits");
        });

        given()
                .when()
                .get("/fruits")
                .then()
                .statusCode(200)
                .body("$", hasSize(fruits.size()))
                .body("name", hasItems("Apple", "Watermelon", "Pineapple"));
    }

    @Test
    void shouldReturnEmptyListIfNoFruitsExist() {
        given()
                .when()
                .get("/fruits")
                .then()
                .statusCode(200)
                .body("$", hasSize(0))
                .body("$", empty());
    }

    @Test
    void shouldGetFruitByIdAndReturn200() {
        FruitDto fruit = new FruitDto(null, "Pineapple", 2.1);

        Number generatedId = given()
                .contentType(ContentType.JSON)
                .body(fruit)
                .when()
                .post("/fruits")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        given()
                .when()
                .get("fruits/{id}", generatedId)
                .then()
                .statusCode(200)
                .body("id", equalTo(generatedId))
                .body("name", is(fruit.name()));
    }

    @Test
    void getById_shouldReturn404IfFruitNotExist() {
        given()
                .when()
                .get("fruits/{id}", 1L)
                .then()
                .statusCode(404)
                .body("message", containsStringIgnoringCase("Fruit with id: 1 not found"));
    }

    @Test
    void shouldUpdateFruitAndReturn200() {
        FruitDto fruit = new FruitDto(null, "Banana", 0.3);
        FruitDto updateFruit = new FruitDto(null, "Canarian Banana", 1.5);

        Number generatedId = given()
                .contentType(ContentType.JSON)
                .body(fruit)
                .post("/fruits")
                .then()
                .extract()
                .path("id");

        given()
                .contentType(ContentType.JSON)
                .body(updateFruit)
                .when()
                .put("/fruits/{id}", generatedId)
                .then()
                .statusCode(200)
                .body("id", equalTo(generatedId))
                .body("name", is(updateFruit.name()))
                .body("weightKg", equalTo(1.5f));
    }

    @Test
    void update_shouldReturn404IfFruitNotExist() {
        FruitDto updateFruit = new FruitDto(null, "Canarian Banana", 1.5);

        given()
                .contentType(ContentType.JSON)
                .body(updateFruit)
                .when()
                .put("/fruits/{id}", 1)
                .then()
                .statusCode(404)
                .body("message", containsStringIgnoringCase("Fruit with id: 1 not found"));
    }

    @Test
    void update_shouldReturn400WhenInvalidData() {
        FruitDto fruit = new FruitDto(null, "Banana", 0.3);
        FruitDto updateFruit = new FruitDto(null, "", -4);

        Number generatedId = given()
                .contentType(ContentType.JSON)
                .body(fruit)
                .post("/fruits")
                .then()
                .extract()
                .path("id");

        given()
                .contentType(ContentType.JSON)
                .body(updateFruit)
                .when()
                .put("/fruits/{id}", generatedId)
                .then()
                .statusCode(400);
    }

    @Test
    void shouldDeleteFruitAndReturn204() {
        FruitDto fruit = new FruitDto(null, "Apple", 0.5);

        Number generatedId = given()
                .contentType(ContentType.JSON)
                .body(fruit)
                .post("/fruits")
                .then()
                .extract()
                .path("id");

        given()
                .when()
                .delete("/fruits/{id}", generatedId)
                .then()
                .statusCode(204);
    }

    @Test
    void delete_shouldReturn404IfFruitNotExist() {
        given()
                .when()
                .delete("/fruits/{id}", 1)
                .then()
                .statusCode(404)
                .body("message", containsStringIgnoringCase("Fruit with id: 1 not found"));
    }
}
