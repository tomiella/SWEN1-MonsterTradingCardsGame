package at.pranjic.application.mtcg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TradingDealDTO {
    @JsonProperty("Id")
    private String id;

    @JsonProperty("CardToTrade")
    private int cardToTrade;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("MinimumDamage")
    private int minimumDamage;

    public TradingDealDTO() {}

    public TradingDealDTO(String id, int cardToTrade, String type, int minimumDamage) {
        this.id = id;
        this.cardToTrade = cardToTrade;
        this.type = type;
        this.minimumDamage = minimumDamage;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCardToTrade() {
        return cardToTrade;
    }

    public void setCardToTrade(int cardToTrade) {
        this.cardToTrade = cardToTrade;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMinimumDamage() {
        return minimumDamage;
    }

    public void setMinimumDamage(int minimumDamage) {
        this.minimumDamage = minimumDamage;
    }
}
