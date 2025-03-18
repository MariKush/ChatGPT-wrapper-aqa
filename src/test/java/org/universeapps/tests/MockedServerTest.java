package org.universeapps.tests;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.Test;
import org.universeapps.domain.RequestBody;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.universeapps.utils.Utils.executeRequest;
import static org.universeapps.utils.Utils.getBearerToken;

public class MockedServerTest {

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


}
