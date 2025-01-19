package at.pranjic.application.mtcg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TradingDeal {
    @JsonProperty("Id")
    private String id;

    @JsonProperty("OfferedCardId")
    private int offeredCardId;

    @JsonProperty("RequestedCardType")
    private String requestedCardType;

    @JsonProperty("RequestedMinDamage")
    private int requestedMinDamage;

    @JsonProperty("UserId")
    private int userId;

    public TradingDeal() {}

    public TradingDeal(String id, int offeredCardId, String requestedCardType, int requestedMinDamage, int userId) {
        this.id = id;
        this.offeredCardId = offeredCardId;
        this.requestedCardType = requestedCardType;
        this.requestedMinDamage = requestedMinDamage;
        this.userId = userId;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getOfferedCardId() { return offeredCardId; }
    public void setOfferedCardId(int offeredCardId) { this.offeredCardId = offeredCardId; }

    public String getRequestedCardType() { return requestedCardType; }
    public void setRequestedCardType(String requestedCardType) { this.requestedCardType = requestedCardType; }

    public int getRequestedMinDamage() { return requestedMinDamage; }
    public void setRequestedMinDamage(int requestedMinDamage) { this.requestedMinDamage = requestedMinDamage; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}