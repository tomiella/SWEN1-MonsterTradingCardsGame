package at.pranjic.application.mtcg;

import at.pranjic.application.mtcg.data.ConnectionPool;
import at.pranjic.application.mtcg.repository.*;
import at.pranjic.application.mtcg.router.ControllerNotFoundException;
import at.pranjic.application.mtcg.router.Route;
import at.pranjic.application.mtcg.router.Router;
import at.pranjic.application.mtcg.service.*;
import at.pranjic.server.Application;
import at.pranjic.server.http.HttpMethod;
import at.pranjic.server.http.HttpStatus;
import at.pranjic.server.http.Request;
import at.pranjic.server.http.Response;
import at.pranjic.application.mtcg.controller.*;
import com.fasterxml.jackson.core.JsonProcessingException;

public class MonsterTradingCardsGameApplication implements Application {

    private final Router router = new Router();

    public MonsterTradingCardsGameApplication() {
        setRoutes();
    }

    public void setRoutes() {
        ConnectionPool connectionPool = new ConnectionPool(10);

        UserRepository userRepository = new UserDbRepository(connectionPool);
        UserService userService = new UserService(userRepository);

        PackageRepository packageRepository = new PackageDbRepository(connectionPool);
        PackageService packageService = new PackageService(packageRepository, userRepository);

        CardRepository cardRepository = new CardDbRepository(connectionPool);
        CardService cardService = new CardService(cardRepository, userRepository);

        DeckRepository deckRepository = new DeckDbRepository(connectionPool);
        DeckService deckService = new DeckService(deckRepository, cardRepository, userRepository);

        StatsRepository statsRepository = new StatsDbRepository(connectionPool);
        StatsService statsService = new StatsService(statsRepository, userRepository);

        ScoreboardRepository scoreboardRepository = new ScoreboardDbRepository(connectionPool);
        ScoreboardService scoreboardService = new ScoreboardService(scoreboardRepository);

        TradingRepository tradingRepository = new TradingDbRepository(connectionPool);
        TradingService tradingService = new TradingService(tradingRepository, cardRepository);

        GameRepository gameRepository = new GameDbRepository(connectionPool);
        GameService gameService = new GameService(userRepository, deckRepository, gameRepository);

        Controller userController = new UserController(userService);
        router.addRoute("/users", userController);
        router.addRoute("/sessions", userController);

        Controller packageController = new PackageController(packageService, cardService);
        router.addRoute("/packages", packageController);
        router.addRoute("/transactions", packageController);

        Controller cardController = new CardController(cardService);
        router.addRoute("/cards", cardController);

        Controller deckController = new DeckController(deckService);
        router.addRoute("/deck", deckController);

        Controller statsController = new StatsController(statsService);
        router.addRoute("/stats", statsController);

        Controller scoreboardController = new ScoreboardController(scoreboardService);
        router.addRoute("/scoreboard", scoreboardController);

        Controller gameController = new GameController(gameService);
        router.addRoute("/battles", gameController);

        Controller tradingController = new TradingController(tradingService);
        router.addRoute("/tradings", tradingController);
    }

    @Override
    public Response handle(Request request) {
        try {
            String path = request.getPath();

            Controller c = router.getController(path);
            return c.handle(request);

        } catch (Exception e) {
            Response response = new Response();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setBody("{\"error\": \"%s\"}".formatted(e.toString()));
            response.setHeader("Content-Type", "application/json");
            return response;
        } catch (ControllerNotFoundException e) {
            Response response = new Response();
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setBody("{\"error\": \"Path not found: %s\"}".formatted(e.toString()));
            response.setHeader("Content-Type", "application/json");
            return response;
        }
    }
}
