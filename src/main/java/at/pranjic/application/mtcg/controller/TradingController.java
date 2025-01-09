package at.pranjic.application.mtcg.controller;

import at.pranjic.application.mtcg.dto.CardDTO;
import at.pranjic.application.mtcg.dto.TradingDealDTO;
import at.pranjic.application.mtcg.entity.TradingDeal;
import at.pranjic.application.mtcg.service.TradingService;
import at.pranjic.server.http.HttpMethod;
import at.pranjic.server.http.HttpStatus;
import at.pranjic.server.http.Request;
import at.pranjic.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

public class TradingController extends Controller {
    private final ObjectMapper mapper = new ObjectMapper();
    private final TradingService tradingService;

    public TradingController(TradingService tradingService) {
        this.tradingService = tradingService;
    }

    @Override
    public Response handle(Request request) throws JsonProcessingException {
        String path = request.getPath();
        HttpMethod method = request.getMethod();

        // Card-related routing
        if (path.equals("/tradings") && method.equals(HttpMethod.GET)) {
            return getAllTradingDeals(request);
        } else if (path.equals("/tradings") && method.equals(HttpMethod.POST)) {
            return null;
        } else if (path.startsWith("/tradings/") && method.equals(HttpMethod.GET)) {
            return null;
        } else if (path.startsWith("/tradings/") && method.equals(HttpMethod.DELETE)) {
            return null;
        }

        Response response = new Response();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setBody("{\"error\": \"Path not found: %s\"}".formatted(path));
        response.setHeader("Content-Type", "application/json");
        return response;
    }

    private Response getAllTradingDeals(Request request) throws JsonProcessingException {
        String header = request.getHeader("authorization");
        if (header.startsWith("Bearer ")) {
            String token = header.substring("Bearer ".length());

            String username = token.split("-")[0];

            List<TradingDealDTO> deals = tradingService.getAllTradingDeals().stream().map(deal -> new TradingDealDTO("" + deal.getId(), deal.getRequestedCardType(), deal.getRequestedCardType(), deal.getRequestedMinDamage())).collect(Collectors.toList());
            return new Response(HttpStatus.OK, mapper.writeValueAsString(deals));
        } else {
            return new Response(HttpStatus.UNAUTHORIZED, "Access token is missing or invalid");
        }
    }

    private Response createTradingDeal(Request request) throws JsonProcessingException {
        String header = request.getHeader("authorization");
        if (header.startsWith("Bearer ")) {
            String token = header.substring("Bearer ".length());

            String username = token.split("-")[0];

            TradingDealDTO deal = mapper.readValue(request.getBody(), TradingDealDTO.class);

            tradingService.createTradingDeal(deal);
            return new Response(HttpStatus.CREATED, "Created trading deal");
        } else {
            return new Response(HttpStatus.UNAUTHORIZED, "Access token is missing or invalid");
        }
    }

}
