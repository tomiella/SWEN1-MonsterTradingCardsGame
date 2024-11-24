package at.pranjic.application.mtcg;

import at.pranjic.application.mtcg.router.ControllerNotFoundException;
import at.pranjic.application.mtcg.router.Route;
import at.pranjic.application.mtcg.router.Router;
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
        Controller userController = new UserController();
        router.addRoute("/users", userController);
        router.addRoute("/sessions", userController);
    }

    @Override
    public Response handle(Request request) {
        try {
            String path = request.getPath();

            Controller c = router.getController(path);
            return c.handle(request);

            // User-related routing
//            if (path.equals("/users") && method.equals(HttpMethod.POST)) {
//                return userController.registerUser(request);
//            } else if (path.startsWith("/users/") && method.equals(HttpMethod.GET)) {
//                return userController.getUser(request);
//            } else if (path.startsWith("/users/") && method.equals(HttpMethod.PUT)) {
//                return userController.updateUser(request);
//            } else if (path.equals("/sessions") && method.equals(HttpMethod.POST)) {
//                return userController.loginUser(request);
//            }

            //        // Package-related routing
            //        else if (path.equals("/packages") && method.equals(HttpMethod.POST)) {
            //            return packageController.createPackage(request);
            //        } else if (path.equals("/transactions/packages") && method.equals(HttpMethod.POST)) {
            //            return packageController.acquirePackage(request);
            //        }
            //
            //        // Card-related routing
            //        else if (path.equals("/cards") && method.equals(HttpMethod.GET)) {
            //            return cardController.getCards(request);
            //        } else if (path.equals("/deck") && method.equals(HttpMethod.GET)) {
            //            return cardController.getDeck(request);
            //        } else if (path.equals("/deck") && method.equals(HttpMethod.PUT)) {
            //            return cardController.configureDeck(request);
            //        }
            //
            //        // Game-related routing
            //        else if (path.equals("/stats") && method.equals(HttpMethod.GET)) {
            //            return gameController.getUserStats(request);
            //        } else if (path.equals("/scoreboard") && method.equals(HttpMethod.GET)) {
            //            return gameController.getScoreboard(request);
            //        } else if (path.equals("/battles") && method.equals(HttpMethod.POST)) {
            //            return gameController.startBattle(request);
            //        }
            //
            //        // Trade-related routing
            //        else if (path.equals("/tradings") && method.equals(HttpMethod.GET)) {
            //            return tradeController.getTradingDeals(request);
            //        } else if (path.equals("/tradings") && method.equals(HttpMethod.POST)) {
            //            return tradeController.createTradingDeal(request);
            //        } else if (path.startsWith("/tradings/") && method.equals(HttpMethod.DELETE)) {
            //            return tradeController.deleteTradingDeal(request);
            //        } else if (path.startsWith("/tradings/") && method.equals(HttpMethod.POST)) {
            //            return tradeController.executeTrade(request);
            //        }
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
