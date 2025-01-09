package at.pranjic.application.mtcg.controller;

import at.pranjic.server.http.HttpMethod;
import at.pranjic.server.http.HttpStatus;
import at.pranjic.server.http.Request;
import at.pranjic.server.http.Response;

public class CardController extends Controller {
    @Override
    public Response handle(Request request) {
        String path = request.getPath();
        HttpMethod method = request.getMethod();

        // Card-related routing
        if (path.equals("/cards") && method.equals(HttpMethod.GET)) {
            return getCards(request);
        } else if (path.equals("/deck") && method.equals(HttpMethod.GET)) {
            return getDeck(request);
        } else if (path.equals("/deck") && method.equals(HttpMethod.PUT)) {
            return configureDeck(request);
        }

        Response response = new Response();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setBody("{\"error\": \"Path not found: %s\"}".formatted(path));
        response.setHeader("Content-Type", "application/json");
        return response;
    }

    private Response configureDeck(Request request) {
        return null;
    }

    private Response getDeck(Request request) {
        return null;
    }

    private Response getCards(Request request) {
        return null;
    }
}
