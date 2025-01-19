package at.pranjic.application.mtcg.controller;

import at.pranjic.application.mtcg.dto.CardDTO;
import at.pranjic.application.mtcg.entity.Card;
import at.pranjic.application.mtcg.service.CardService;
import at.pranjic.application.mtcg.service.UserService;
import at.pranjic.server.http.HttpMethod;
import at.pranjic.server.http.HttpStatus;
import at.pranjic.server.http.Request;
import at.pranjic.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

public class CardController extends Controller {
    private final ObjectMapper mapper = new ObjectMapper();
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @Override
    public Response handle(Request request) {
        String path = request.getPath();
        HttpMethod method = request.getMethod();

        // Card-related routing
        try {
            if (path.equals("/cards") && method.equals(HttpMethod.GET)) {
                return getCards(request);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Response response = new Response();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setBody("{\"error\": \"Path not found: %s\"}".formatted(path));
        response.setHeader("Content-Type", "application/json");
        return response;
    }

    private Response getCards(Request request) throws JsonProcessingException {
        String header = request.getHeader("authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring("Bearer ".length());

            String username = token.split("-")[0];

            if (!UserService.checkAuth(username, header)) {
                return new Response(HttpStatus.UNAUTHORIZED, "Access token is missing or invalid");
            }

            List<CardDTO> cards = cardService.getAllCardsByUserId(username).stream().map(card -> new CardDTO(card.getId(), card.getName(), card.getDamage())).collect(Collectors.toList());
            return new Response(HttpStatus.OK, mapper.writeValueAsString(cards));
        }
        return new Response(HttpStatus.UNAUTHORIZED, "Access token is missing or invalid");
    }
}
