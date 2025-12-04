package com.ckn.microservices.product;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;

import javax.management.timer.TimerMBean;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

    @LocalServerPort
    private Integer port;

    @Autowired
    MongoDBContainer mongoDbContainer;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI="http://localhost";
        RestAssured.port=port;
    }

    @Test
    void containerIsRunning() {
        assertTrue(mongoDbContainer.isRunning(), "MongoDB container should be running");
    }

    @Test
	void shouldCreateProduct() {

        String requestBody = """
                {
                    "name": "iPhone 13",
                    "description": "Latest Apple iPhone model",
                    "price": 9999
                }
                """;
        RestAssured.given().contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/product")
                .then()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
        .body("name", Matchers.equalTo("iPhone 13"))
        .body("description", Matchers.equalTo("Latest Apple iPhone model"))
        .body("price", Matchers.equalTo(9999));
	}

}
