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

        TradingDeal newTradingDeal = new TradingDeal(tradingDeal.getId(), tradingDeal.getCardToTrade(), tradingDeal.getType(), tradingDeal.getMinimumDamage(), 1);

        tradingRepository.createTradingDeal(newTradingDeal);
    }

    public List<TradingDeal> getAllTradingDeals() {
        return tradingRepository.getAllTradingDeals();
    }

    public TradingDeal getTradingDealById(int tradingDealId) {
        return tradingRepository.getTradingDealById(tradingDealId)
                .orElseThrow(() -> new IllegalArgumentException("Trading deal not found"));
    }

    public void deleteTradingDeal(int tradingDealId) {
        tradingRepository.deleteTradingDeal(tradingDealId);
    }
}

