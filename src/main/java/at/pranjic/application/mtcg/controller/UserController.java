package at.pranjic.application.mtcg.controller;

import at.pranjic.application.mtcg.dto.UserDTO;
import at.pranjic.application.mtcg.entity.User;
import at.pranjic.application.mtcg.service.UserService;
import at.pranjic.server.http.HttpMethod;
import at.pranjic.server.http.HttpStatus;
import at.pranjic.server.http.Request;
import at.pranjic.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

public class UserController extends Controller {
    private final ObjectMapper mapper = new ObjectMapper();
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Response handle(Request request) {
        String path = request.getPath();
        HttpMethod method = request.getMethod();

        try {
            if (path.equals("/users") && method.equals(HttpMethod.POST)) {
                return registerUser(request);
            } else if (path.startsWith("/users/") && method.equals(HttpMethod.GET)) {
                return getUser(request);
            } else if (path.startsWith("/users/") && method.equals(HttpMethod.PUT)) {
                return updateUser(request);
            } else if (path.equals("/sessions") && method.equals(HttpMethod.POST)) {
                return loginUser(request);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Response response = new Response();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setBody("{\"error\": \"Path not found: %s\"}".formatted(path));
        response.setHeader("Content-Type", "application/json");
        return response;
    }

    public Response registerUser(Request request) {
        User user = fromBody(request.getBody(), User.class);

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

    public Response getUser(Request request) {
        String[] pathSegments = request.getPath().split("/");

        if (pathSegments.length != 3) {
            return new Response(HttpStatus.BAD_REQUEST, "Invalid path");
        }

        String username = pathSegments[2];

        if (username.isEmpty()) {
            return new Response(HttpStatus.BAD_REQUEST, "Invalid username");
        }

        String token = request.getHeader("authorization");
        if (!userService.checkAuth(username, token)) {
            return new Response(HttpStatus.UNAUTHORIZED, "Access token is missing or invalid");
        }

        Optional<UserDTO> userDTO = userService.getUser(username);

        if (userDTO.isPresent()) {
            return json(HttpStatus.OK, userDTO);
        }
        return new Response(HttpStatus.NOT_FOUND, "User not found");
    }

    public Response updateUser(Request request) {
        String[] pathSegments = request.getPath().split("/");

        if (pathSegments.length != 3) {
            return new Response(HttpStatus.BAD_REQUEST, "Invalid path");
        }

        String username = pathSegments[2];

        if (username.isEmpty()) {
            return new Response(HttpStatus.BAD_REQUEST, "Invalid username");
        }

        String token = request.getHeader("authorization");
        if (!userService.checkAuth(username, token)) {
            return new Response(HttpStatus.UNAUTHORIZED, "Access token is missing or invalid");
        }

        UserDTO user = fromBody(request.getBody(), UserDTO.class);

        boolean success = userService.updateUser(username, user);
        if (success) {
            return new Response(HttpStatus.OK, "User updated successfully");
        }
        return new Response(HttpStatus.NOT_FOUND, "User not found");
    }
}
