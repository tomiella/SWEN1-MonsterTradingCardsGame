package at.pranjic.application.mtcg.controller;

import at.pranjic.application.mtcg.dto.UserDTO;
import at.pranjic.application.mtcg.entity.User;
import at.pranjic.application.mtcg.service.UserService;
import at.pranjic.server.http.HttpStatus;
import at.pranjic.server.http.Request;
import at.pranjic.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

public class UserController {
    private final ObjectMapper mapper = new ObjectMapper();
    private final UserService userService = new UserService();

    public Response registerUser(Request request) throws JsonProcessingException {
        User user = mapper.readValue(request.getBody(), User.class);

        boolean success = userService.registerUser(user);
        if (success) {
            return new Response(HttpStatus.CREATED, "User registered successfully");
        }
        return new Response(HttpStatus.CONFLICT, "User already exists");
    }

    public Response loginUser(Request request) throws JsonProcessingException {
        JsonNode jsonNode = mapper.readTree(request.getBody());
        String username = jsonNode.get("Username").asText();
        String password = jsonNode.get("Password").asText();

        if (username == null || password == null) {
            return new Response(HttpStatus.BAD_REQUEST, "Username and password are required");
        }

        Optional<String> token = userService.loginUser(username, password);
        if (token.isEmpty()) {
            return new Response(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
        return new Response(HttpStatus.OK, token.get());
    }

    public Response getUser(Request request) throws JsonProcessingException {
        String[] pathSegments = request.getPath().split("/");

        if (pathSegments.length != 3) {
            return new Response(HttpStatus.BAD_REQUEST, "Invalid path");
        }

        String username = pathSegments[2];

        if (username.isEmpty()) {
            return new Response(HttpStatus.BAD_REQUEST, "Invalid username");
        }

        if (!userService.checkAuth(username, request)) {
            return new Response(HttpStatus.UNAUTHORIZED, "Access token is missing or invalid");
        }

        Optional<UserDTO> userDTO = userService.getUser(username);

        if (userDTO.isPresent()) {
            String body = mapper.writeValueAsString(userDTO.get());
            return new Response(HttpStatus.OK, body);
        }
        return new Response(HttpStatus.NOT_FOUND, "User not found");
    }

    public Response updateUser(Request request) throws JsonProcessingException {
        String[] pathSegments = request.getPath().split("/");

        if (pathSegments.length != 3) {
            return new Response(HttpStatus.BAD_REQUEST, "Invalid path");
        }

        String username = pathSegments[2];

        if (username.isEmpty()) {
            return new Response(HttpStatus.BAD_REQUEST, "Invalid username");
        }

        if (!userService.checkAuth(username, request)) {
            return new Response(HttpStatus.UNAUTHORIZED, "Access token is missing or invalid");
        }

        UserDTO user = mapper.readValue(request.getBody(), UserDTO.class);

        boolean success = userService.updateUser(username, user);
        if (success) {
            return new Response(HttpStatus.OK, "User updated successfully");
        }
        return new Response(HttpStatus.NOT_FOUND, "User not found");
    }
}
