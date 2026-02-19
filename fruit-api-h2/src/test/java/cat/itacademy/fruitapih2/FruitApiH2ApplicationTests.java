package cat.itacademy.fruitapih2;

import cat.itacademy.fruitapih2.dto.FruitDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
}
