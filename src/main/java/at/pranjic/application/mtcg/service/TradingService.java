package at.pranjic.application.mtcg.service;

import at.pranjic.application.mtcg.dto.TradingDealDTO;
import at.pranjic.application.mtcg.entity.Card;
import at.pranjic.application.mtcg.entity.CardInfo;
import at.pranjic.application.mtcg.entity.TradingDeal;
import at.pranjic.application.mtcg.repository.CardRepository;
import at.pranjic.application.mtcg.repository.TradingRepository;

import java.util.List;
import java.util.Optional;

public class TradingService {
    private final TradingRepository tradingRepository;
    private final CardRepository cardRepository;

    public TradingService(TradingRepository tradingRepository, CardRepository cardRepository) {
        this.tradingRepository = tradingRepository;
        this.cardRepository = cardRepository;
    }

    public void createTradingDeal(TradingDealDTO tradingDeal) {
        CardInfo info = CardInfo.fromDisplayName(tradingDeal.getCardToTrade());
        TradingDeal newTradingDeal = new TradingDeal(tradingDeal.getId(), tradingDeal.getCardToTrade(), info.getType(), info.getElement(), tradingDeal.getMinimumDamage(), 1L, false);

        tradingRepository.createTradingDeal(newTradingDeal);
    }

    public List<TradingDeal> getAllTradingDeals() {
        return tradingRepository.getAllTradingDeals();
    }

    public TradingDeal getTradingDealById(String tradingDealId) {
        return tradingRepository.getTradingDealById(tradingDealId)
                .orElseThrow(() -> new IllegalArgumentException("Trading deal not found"));
    }

    public void deleteTradingDeal(String tradingDealId) {
        tradingRepository.deleteTradingDeal(tradingDealId);
    }
}

