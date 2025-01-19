package at.pranjic.application.mtcg.controller;

import at.pranjic.application.mtcg.service.GameService;
import at.pranjic.application.mtcg.service.UserService;
import at.pranjic.server.http.HttpMethod;
import at.pranjic.server.http.HttpStatus;
import at.pranjic.server.http.Request;
import at.pranjic.server.http.Response;

public class GameController extends Controller {
    GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public Response handle(Request request) {
        String path = request.getPath();
        HttpMethod method = request.getMethod();

        // Game-related routing
        if (path.equals("/battles") && method.equals(HttpMethod.POST)) {
            return startBattle(request);
        }

        Response response = new Response();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setBody("{\"error\": \"Path not found: %s\"}".formatted(path));
        response.setHeader("Content-Type", "application/json");
        return response;
    }

    private Response startBattle(Request request) {
        String header = request.getHeader("authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring("Bearer ".length());

            String username = token.split("-")[0];

            if (!UserService.checkAuth(username, header)) {
                return new Response(HttpStatus.UNAUTHORIZED, "Access token is missing or invalid");
            }

            String log = gameService.startBattle(username);
            System.out.println(log);
            return new Response(HttpStatus.OK, log);
        } else {
            return new Response(HttpStatus.UNAUTHORIZED, "Access token is missing or invalid");
        }
    }
}
