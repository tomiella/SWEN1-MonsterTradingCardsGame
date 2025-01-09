package at.pranjic.application.mtcg.repository;

import at.pranjic.application.mtcg.entity.TradingDeal;

import java.util.List;
import java.util.Optional;

public interface TradingRepository {
    void createTradingDeal(TradingDeal tradingDeal);
    List<TradingDeal> getAllTradingDeals();
    Optional<TradingDeal> getTradingDealById(String tradingDealId);
    void deleteTradingDeal(String tradingDealId);
}
