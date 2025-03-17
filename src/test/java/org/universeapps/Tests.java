package org.universeapps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.io.FileInputStream;
import java.io.IOException;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


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


    private Response executeRequest(RequestBody requestBody) {
        try {
            return given()
                    .header("Authorization", "Bearer " + getBearerToken())
                    .contentType(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(requestBody))
                    .when()
                    .post(BASE_URL + ENDPOINT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void positiveTest() {
        Response response = executeRequest(RequestBody.withDefault());
        JsonPath responseJsonPath = response.then()
                .statusCode(200)
                .extract().jsonPath();

        assertAll(
                () -> assertThat(responseJsonPath.get("id"), matchesPattern("^chatcmpl-[A-Za-z0-9]{29}$")),
                () -> assertEquals("chat.completion", responseJsonPath.get("object")),
                () -> assertThat(responseJsonPath.get("choices[0].message.content"), startsWith("I can't provide"))
        );
    }
}
