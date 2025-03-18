package org.universeapps.tests;

import io.restassured.parsing.Parser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.universeapps.domain.RequestBody;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.matchesPattern;
import static org.universeapps.utils.Utils.executeRequest;
import static org.universeapps.utils.Utils.getBearerToken;

@Execution(ExecutionMode.CONCURRENT)
public class SingleMessageTest {

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
    public void emptyMessagesTest() {
        executeRequest(RequestBody.withDefault().setMessages(new ArrayList<>()))
                .then()
                .statusCode(400)
                .body("message", equalTo("Request failed with status code 400"));
    }

}
