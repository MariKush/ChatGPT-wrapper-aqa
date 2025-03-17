package org.universeapps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.io.FileInputStream;
import java.util.ArrayList;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class Tests {

    private static final String BASE_URL = "https://automation-qa-test.universeapps.limited";
    private static final String ENDPOINT = "/stream/v1/chat/completions";
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(SNAKE_CASE);

    private String getBearerToken() {
        try (FileInputStream file = new FileInputStream("token.txt")) {
            return new String(file.readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Response executeRequest(RequestBody requestBody) {
        return executeRequest(requestBody, getBearerToken());
    }

    private Response executeRequest(RequestBody requestBody, String token) {
        try {
            return given()
                    .header("Authorization", "Bearer " + token)
                    .contentType(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(requestBody))
                    .when()
                    .post(BASE_URL + ENDPOINT)
                    .then().log().all()
                    .extract().response();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void positiveTest() {
        executeRequest(RequestBody.withDefault())
                .then()
                .statusCode(200)
                .defaultParser(Parser.JSON)
                .body("id", matchesPattern("^chatcmpl-[A-Za-z0-9]{29}$"))
                .body("object", equalTo("chat.completion"))
                .body("choices[0].message.content", startsWith("I can't provide"));
    }

    @Test
    public void invalidTokenTest() {
        executeRequest(RequestBody.withDefault(), "t" + getBearerToken())
                .then()
                .statusCode(401)
                .body("message", equalTo("Unauthorized"));
    }

    @Test
    public void emptyMessageTest() {
        executeRequest(RequestBody.withDefault().setMessages(new ArrayList<>()))
                .then()
                .statusCode(400)
                .body("message", equalTo("Request failed with status code 400"));
    }
}
