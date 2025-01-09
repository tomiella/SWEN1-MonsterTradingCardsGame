package at.pranjic.application.mtcg.controller;

import at.pranjic.server.http.HttpMethod;
import at.pranjic.server.http.HttpStatus;
import at.pranjic.server.http.Request;
import at.pranjic.server.http.Response;

public class GameController extends Controller {
    @Override
    public Response handle(Request request) {
        String path = request.getPath();
        HttpMethod method = request.getMethod();

        // Game-related routing
        if (path.equals("/stats") && method.equals(HttpMethod.GET)) {
            return getUserStats(request);
        } else if (path.equals("/scoreboard") && method.equals(HttpMethod.GET)) {
            return getScoreboard(request);
        } else if (path.equals("/battles") && method.equals(HttpMethod.POST)) {
            return startBattle(request);
        }

        Response response = new Response();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setBody("{\"error\": \"Path not found: %s\"}".formatted(path));
        response.setHeader("Content-Type", "application/json");
        return response;
    }

    private Response startBattle(Request request) {
        return null;
    }

    private Response getScoreboard(Request request) {
        return null;
    }

    private Response getUserStats(Request request) {
        return null;
    }
}
