package org.universeapps.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.universeapps.domain.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.universeapps.utils.Utils.executeRequest;
import static org.universeapps.utils.Utils.objectMapper;

public class StreamTest {


    private List<JsonNode> getStreamingResponse(){
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

        return jsonList;
    }

    @Test
    public void sizeBiggerThanZero() {
        List<JsonNode> streaming = getStreamingResponse();

        assertThat(streaming, hasSize(greaterThan(0)));
    }

    @Test
    public void contentHasLastItemWithFinishReason() {
        List<JsonNode> streaming = getStreamingResponse();

        JsonNode lastItem = streaming.get(streaming.size() - 1);
        JsonNode choice = lastItem.get("choices").get(0);
        assertThat(lastItem.asText(), choice.get("finish_reason").asText(), equalTo("stop"));
        assertThat(lastItem.asText(), choice.get("delta").size(), equalTo(0));
    }

    @Test
    public void contentHasEnoughCharacters() {
        List<JsonNode> streaming = getStreamingResponse();

        String responseMessage = streaming.stream()
                .map(r -> r.get("choices").get(0).get("delta").get("content"))
                .filter(Objects::nonNull)
                .map(JsonNode::textValue)
                .collect(Collectors.joining(""));

        assertThat(responseMessage, responseMessage.length(), greaterThan(20));
    }

    @Test
    public void contentHasSameId() {
        List<JsonNode> streaming = getStreamingResponse();

        Set<String> ids = streaming.stream()
                .map(r -> r.get("id").asText())
                .collect(Collectors.toSet());

        assertThat(ids, hasSize(1));
    }


}
