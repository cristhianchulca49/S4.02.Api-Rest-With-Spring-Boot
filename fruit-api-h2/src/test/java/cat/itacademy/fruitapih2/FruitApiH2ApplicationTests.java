package cat.itacademy.fruitapih2;

import cat.itacademy.fruitapih2.dto.FruitDto;
import cat.itacademy.fruitapih2.repository.FruitRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FruitApiH2ApplicationTests {

    @LocalServerPort
    private int randomPort;

    @Autowired
    private FruitRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
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
}
