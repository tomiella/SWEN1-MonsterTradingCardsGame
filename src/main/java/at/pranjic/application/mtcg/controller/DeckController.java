package at.pranjic.application.mtcg.controller;

import at.pranjic.application.mtcg.dto.CardDTO;
import at.pranjic.application.mtcg.entity.Card;
import at.pranjic.application.mtcg.service.CardService;
import at.pranjic.application.mtcg.service.DeckService;
import at.pranjic.application.mtcg.service.UserService;
import at.pranjic.server.http.HttpMethod;
import at.pranjic.server.http.HttpStatus;
import at.pranjic.server.http.Request;
import at.pranjic.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

public class DeckController extends Controller {
    private final ObjectMapper mapper = new ObjectMapper();
    private final DeckService deckService;

    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }

    @Override
    public Response handle(Request request) throws JsonProcessingException {
        String path = request.getPath();
        HttpMethod method = request.getMethod();

        // Card-related routing
        if (path.equals("/deck") && method.equals(HttpMethod.GET)) {
            return getDeck(request);
        } else if (path.equals("/deck?format=plain") && method.equals(HttpMethod.GET)) {
            return getDeckPlain(request);
        } else if (path.equals("/deck") && method.equals(HttpMethod.PUT)) {
            return configureDeck(request);
        }

        Response response = new Response();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setBody("{\"error\": \"Path not found: %s\"}".formatted(path));
        response.setHeader("Content-Type", "application/json");
        return response;
    }

    private Response configureDeck(Request request) throws JsonProcessingException {
        String header = request.getHeader("authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring("Bearer ".length());

            String username = token.split("-")[0];

            if (!UserService.checkAuth(username, header)) {
                return new Response(HttpStatus.UNAUTHORIZED, "Access token is missing or invalid");
            }

            List<String> cardIds = mapper.readValue(request.getBody(), mapper.getTypeFactory().constructCollectionType(List.class, String.class));
            try {
                deckService.configureDeck(username, cardIds);
            } catch (IllegalArgumentException e) {
                return new Response(HttpStatus.BAD_REQUEST, e.getMessage());
            }
            return new Response(HttpStatus.OK, "");
        } else {
            return new Response(HttpStatus.UNAUTHORIZED, "Access token is missing or invalid");
        }
    }

    private Response getDeckPlain(Request request) throws JsonProcessingException {
        String header = request.getHeader("authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring("Bearer ".length());

            String username = token.split("-")[0];

            if (!UserService.checkAuth(username, header)) {
                return new Response(HttpStatus.UNAUTHORIZED, "Access token is missing or invalid");
            }

            List<CardDTO> deck = deckService.getDeck(username).stream().map(card -> new CardDTO(card.getId(), card.getName(), card.getDamage())).collect(Collectors.toList());
            StringBuilder body = new StringBuilder();
            for (CardDTO card : deck) {
                body.append("Id: %s, Name: %s, Damage: %s".formatted(card.getId(), card.getName(), card.getDamage())).append("\n");
            }
            Response res = new Response(HttpStatus.OK, body.toString());
            res.setHeader("Content-Type", "plain/txt");
            return res;
        } else {
            return new Response(HttpStatus.UNAUTHORIZED, "Access token is missing or invalid");
        }
    }

    private Response getDeck(Request request) throws JsonProcessingException {
        String header = request.getHeader("authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring("Bearer ".length());

            String username = token.split("-")[0];

            if (!UserService.checkAuth(username, header)) {
                return new Response(HttpStatus.UNAUTHORIZED, "Access token is missing or invalid");
            }

            List<CardDTO> deck = deckService.getDeck(username).stream().map(card -> new CardDTO(card.getId(), card.getName(), card.getDamage())).collect(Collectors.toList());
            return new Response(HttpStatus.OK, mapper.writeValueAsString(deck));
        } else {
            return new Response(HttpStatus.UNAUTHORIZED, "Access token is missing or invalid");
        }
    }

}
