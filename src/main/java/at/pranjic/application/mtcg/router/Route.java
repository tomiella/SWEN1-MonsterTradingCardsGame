package at.pranjic.application.mtcg.router;

import at.pranjic.application.mtcg.controller.Controller;
import at.pranjic.server.http.HttpMethod;

public class Route {
    private final String route;
    private final Controller controller;

    public Route(String route, Controller controller) {
        this.route = route;
        this.controller = controller;
    }

    public String getRoute() {
        return route;
    }

    public Controller getController() {
        return controller;
    }
}
