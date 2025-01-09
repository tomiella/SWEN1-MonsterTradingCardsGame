package at.pranjic.application.mtcg.controller;

import at.pranjic.server.http.HttpMethod;
import at.pranjic.server.http.HttpStatus;
import at.pranjic.server.http.Request;
import at.pranjic.server.http.Response;

public class TradeController extends Controller {
    @Override
    public Response handle(Request request) {
        String path = request.getPath();
        HttpMethod method = request.getMethod();

        // Trade-related routing
        if (path.equals("/tradings") && method.equals(HttpMethod.GET)) {
            return getTradingDeals(request);
        } else if (path.equals("/tradings") && method.equals(HttpMethod.POST)) {
            return createTradingDeal(request);
        } else if (path.startsWith("/tradings/") && method.equals(HttpMethod.DELETE)) {
            return deleteTradingDeal(request);
        } else if (path.startsWith("/tradings/") && method.equals(HttpMethod.POST)) {
            return executeTrade(request);
        }

        Response response = new Response();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setBody("{\"error\": \"Path not found: %s\"}".formatted(path));
        response.setHeader("Content-Type", "application/json");
        return response;
    }

    private Response executeTrade(Request request) {
        return null;
    }

    private Response deleteTradingDeal(Request request) {
        return null;
    }

    private Response createTradingDeal(Request request) {
        return null;
    }

    private Response getTradingDeals(Request request) {
        return null;
    }
}
