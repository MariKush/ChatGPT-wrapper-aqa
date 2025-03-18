package org.universeapps.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.universeapps.domain.RequestBody;

import java.io.FileInputStream;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE;
import static io.restassured.RestAssured.given;

public class Utils {

    private static final String BASE_URL = "https://automation-qa-test.universeapps.limited";
    private static final String ENDPOINT = "/stream/v1/chat/completions";

    public static final ObjectMapper objectMapper = new ObjectMapper().setPropertyNamingStrategy(SNAKE_CASE);

    public static String getBearerToken() {
        return TokenProvider.getToken();
    }

    public static Response executeRequest(RequestBody requestBody) {
        return executeRequest(BASE_URL, requestBody, getBearerToken());
    }

    public static Response executeRequest(RequestBody requestBody, String token) {
        return executeRequest(BASE_URL, requestBody, token);
    }

    public static Response executeRequest(String baseUrl, RequestBody requestBody, String token) {
        try {
            return given()
                    .header("Authorization", "Bearer " + token)
                    .contentType(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(requestBody))
                    .when()
                    .post(baseUrl + ENDPOINT)
                    .then()
                    .log().all()
                    .extract().response();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
