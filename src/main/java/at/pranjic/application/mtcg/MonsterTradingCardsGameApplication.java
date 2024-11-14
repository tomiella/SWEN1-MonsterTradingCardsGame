package at.pranjic.application.mtcg;

import at.pranjic.server.Application;
import at.pranjic.server.http.HttpStatus;
import at.pranjic.server.http.Request;
import at.pranjic.server.http.Response;
import at.pranjic.application.mtcg.controller.*;

public class MonsterTradingCardsGameApplication implements Application {
    private final UserController userController;
    private final PackageController packageController;
    private final CardController cardController;
    private final GameController gameController;
    private final TradeController tradeController;

    public MonsterTradingCardsGameApplication() {
        this.userController = new UserController();
        this.packageController = new PackageController();
        this.cardController = new CardController();
        this.gameController = new GameController();
        this.tradeController = new TradeController();
    }

    @Override
    public Response handle(Request request) {
        switch (request.getPath()) {
            case String s when s.startsWith("/users") -> {
                return null;
            }
            case String s when s.startsWith("/sessions") -> {
                return null;
            }
            case String s when s.startsWith("/packages") -> {
                return null;
            }
            case String s when s.startsWith("/transactions") -> {
                return null;
            }
            case String s when s.startsWith("/cards") -> {
                return null;
            }
            case String s when s.startsWith("/deck") -> {
                return null;
            }
            case String s when s.startsWith("/stats") -> {
                return null;
            }
            case String s when s.startsWith("/scoreboard") -> {
                return null;
            }
            case String s when s.startsWith("/battles") -> {
                return null;
            }
            case String s when s.startsWith("/tradings") -> {
                return null;
            }
            default -> {
                Response response = new Response();
                response.setStatus(HttpStatus.NOT_IMPLEMENTED);
                response.setBody("{\"error\": \"Path not found: %s\"}".formatted(request.getPath()));
                response.setHeader("Content-Type", "application/json");
                return response;
            }
        }
    }
}
