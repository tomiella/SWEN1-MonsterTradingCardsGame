package at.pranjic.application.mtcg.controller;

import at.pranjic.application.mtcg.entity.UserStats;
import at.pranjic.application.mtcg.service.StatsService;
import at.pranjic.server.http.HttpMethod;
import at.pranjic.server.http.HttpStatus;
import at.pranjic.server.http.Request;
import at.pranjic.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StatsController extends Controller {
    private final ObjectMapper mapper = new ObjectMapper();
    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @Override
    public Response handle(Request request) throws JsonProcessingException {
        String path = request.getPath();
        HttpMethod method = request.getMethod();

        // Card-related routing
        if (path.equals("/stats") && method.equals(HttpMethod.GET)) {
            return getStats(request);
        }

        Response response = new Response();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setBody("{\"error\": \"Path not found: %s\"}".formatted(path));
        response.setHeader("Content-Type", "application/json");
        return response;
    }

    private Response getStats(Request request) throws JsonProcessingException {
        String header = request.getHeader("authorization");
        if (header.startsWith("Bearer ")) {
            String token = header.substring("Bearer ".length());

            String username = token.split("-")[0];

            UserStats stats = statsService.getStats(username);
            return new Response(HttpStatus.OK, mapper.writeValueAsString(stats));
        } else {
            return new Response(HttpStatus.UNAUTHORIZED, "Access token is missing or invalid");
        }
    }
}

