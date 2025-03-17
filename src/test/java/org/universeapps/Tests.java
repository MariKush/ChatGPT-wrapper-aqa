package org.universeapps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;
import java.io.FileInputStream;
import java.io.IOException;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class Tests {

    private static final String BASE_URL = "https://automation-qa-test.universeapps.limited";
    private static final String ENDPOINT = "/stream/v1/chat/completions";
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(SNAKE_CASE);

    private String getBearerToken() throws IOException {
        try (FileInputStream file = new FileInputStream("token.txt")) {
            return new String(file.readAllBytes());
        }
    }


    private ValidatableResponse executeRequest(RequestBody requestBody) {
        try {
            return given()
                    .header("Authorization", "Bearer " + getBearerToken())
                    .contentType(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(requestBody))
                    .when()
                    .post(BASE_URL + ENDPOINT)
                    .then()
                    .assertThat();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void firstTest() {
        executeRequest(RequestBody.withDefault())
//                .statusCode(200)
                .body(startsWith("data"));
    }
}
