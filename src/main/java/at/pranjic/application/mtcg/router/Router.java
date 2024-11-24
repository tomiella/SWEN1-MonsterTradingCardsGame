package at.pranjic.application.mtcg.router;

import at.pranjic.application.mtcg.controller.*;
import at.pranjic.server.http.HttpMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Router {
    private final List<Route> routes = new ArrayList<>();

    public Controller getController(String path) throws ControllerNotFoundException {
        for (Route route : routes) {
            if (!path.startsWith(route.getRoute())) {
               continue;
            }

            return route.getController();
        }

        throw new ControllerNotFoundException(path);
    }

    public void addRoute(String route, Controller controller) {
        routes.add(new Route(route, controller));
    }
}
