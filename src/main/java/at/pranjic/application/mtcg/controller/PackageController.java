package at.pranjic.application.mtcg.controller;

import at.pranjic.application.mtcg.entity.Card;
import at.pranjic.application.mtcg.entity.CardInfo;
import at.pranjic.application.mtcg.exceptions.NoPackagesAvailableException;
import at.pranjic.application.mtcg.exceptions.NotEnoughMoneyException;
import at.pranjic.application.mtcg.service.CardService;
import at.pranjic.application.mtcg.service.UserService;
import at.pranjic.server.http.HttpMethod;
import at.pranjic.server.http.HttpStatus;
import at.pranjic.server.http.Request;
import at.pranjic.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import at.pranjic.application.mtcg.service.PackageService;
import at.pranjic.application.mtcg.entity.Package;

import java.util.ArrayList;
import java.util.List;

public class PackageController extends Controller {
    private final PackageService packageService;
    private final CardService cardService;

    public PackageController(PackageService packageService, CardService cardService) {
        this.packageService = packageService;
        this.cardService = cardService;
    }

    @Override
    public Response handle(Request request) throws JsonProcessingException {
        String path = request.getPath();
        HttpMethod method = request.getMethod();

        // Package-related routing
        if (path.equals("/packages") && method.equals(HttpMethod.POST)) {
            return createPackage(request);
        } else if (path.equals("/transactions/packages") && method.equals(HttpMethod.POST)) {
            return acquirePackage(request);
        }

        Response response = new Response();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setBody("{\"error\": \"Path not found: %s\"}".formatted(path));
        response.setHeader("Content-Type", "application/json");
        return response;
    }

    private Response acquirePackage(Request request) {
        String header = request.getHeader("authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring("Bearer ".length());

            String username = token.split("-")[0];

            if (!UserService.checkAuth(username, header)) {
                return new Response(HttpStatus.UNAUTHORIZED, "Access token is missing or invalid");
            }

            try {
                packageService.acquirePackage(username);
            } catch (NotEnoughMoneyException | NoPackagesAvailableException e) {
                return new Response(HttpStatus.BAD_REQUEST, e.getMessage());
            }
            return new Response(HttpStatus.CREATED, null);
        } else {
            return new Response(HttpStatus.UNAUTHORIZED, "Access token is missing or invalid");
        }
    }

    private Response createPackage(Request request) throws JsonProcessingException {
        String token = request.getHeader("authorization");
        if (!UserService.checkAuth("admin", token)) {
            return new Response(HttpStatus.UNAUTHORIZED, "Access token is missing or invalid");
        }

        Package pkg = new Package(0);
        List<String> cards = new ArrayList<>();
        JsonNode jsonNode = toJsonNode(request.getBody());
        if (jsonNode.isArray()) {
            for (int i = 0; i < jsonNode.size(); i++) {
                JsonNode card = jsonNode.get(i);
                Card cardObj = new Card();
                cardObj.setId(card.get("Id").asText());
                cardObj.setDamage(card.get("Damage").asInt());
                cardObj.setInfo(CardInfo.fromDisplayName(card.get("Name").asText()));

                cardService.addCard(cardObj);

                cards.add(cardObj.getId());
            }
        }

        packageService.addPackage(pkg, cards);
        return new Response(HttpStatus.CREATED, null);
    }
}
