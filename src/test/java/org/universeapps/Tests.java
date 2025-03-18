package org.universeapps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.Test;
import org.universeapps.domain.RequestBody;

import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.matchesPattern;
import static org.universeapps.utils.Utils.executeRequest;
import static org.universeapps.utils.Utils.getBearerToken;
import static org.universeapps.utils.Utils.objectMapper;

public class Tests {

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

    @Test
    public void mockedServerErrorTest() {
        WireMockServer wireMockServer = new WireMockServer(8080);
        try {
            wireMockServer.start();
            wireMockServer.stubFor(post(anyUrl())
                    .willReturn(aResponse()
                            .withStatus(500)
                            .withBody("{\"error\": \"Internal Server Error\", \"message\": \"Something went wrong\"}")
                            .withHeader("Content-Type", "application/json")));

            executeRequest("http://localhost:8080", RequestBody.withDefault(), getBearerToken())
                    .then()
                    .statusCode(500)
                    .body("message", equalTo("Something went wrong"))
                    .body("error", equalTo("Internal Server Error"));
        } finally {
            wireMockServer.stop();
        }
    }

    @Test
    public void positiveStreamingTest() {
        String response = executeRequest(RequestBody.withDefault().setStream(true))
                .then()
                .statusCode(200)
                .extract().body().asString();


        List<JsonNode> jsonList = new ArrayList<>();

        List<String> chunks = response.lines().filter(s -> !s.isBlank()).toList();

        assertThat(chunks.get(chunks.size() - 1), equalTo("data: [DONE]") );

        chunks.forEach(line -> {
            if (line.startsWith("data: ") && !line.equals("data: [DONE]")) {
                String jsonPart = line.substring(6);
                try {
                    jsonList.add(objectMapper.readTree(jsonPart));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        assertThat(jsonList, hasSize(greaterThan(0)));

    }


}
