package at.pranjic.application.mtcg.controller;

import at.pranjic.application.mtcg.exceptions.InvalidBodyException;
import at.pranjic.application.mtcg.exceptions.JsonParserException;
import at.pranjic.server.http.HttpStatus;
import at.pranjic.server.http.Request;
import at.pranjic.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Controller {
    public abstract Response handle(Request request) throws JsonProcessingException;

    private final ObjectMapper objectMapper;

    public Controller() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(
                MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true
        );
    }

    protected <T> T fromBody(String body, Class<T> type) {
        try {
            return objectMapper.readValue(body, type);
        } catch (JsonProcessingException e) {
            throw new InvalidBodyException(e);
        }
    }

    protected JsonNode toJsonNode(String body) throws JsonProcessingException {
        return this.objectMapper.readTree(body);
    }

    protected Response json(HttpStatus status, Object object) {
        Response response = new Response();
        response.setStatus(status);
        response.setHeader("Content-Type", "application/json");
        try {
            response.setBody(objectMapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            throw new JsonParserException(e);
        }

        return response;
    }

}
