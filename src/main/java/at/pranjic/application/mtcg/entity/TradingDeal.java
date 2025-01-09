package at.pranjic.application.mtcg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TradingDeal {
    @JsonProperty("Id")
    private String id;

    @JsonProperty("OfferedCardId")
    private String offeredCardId;

    @JsonProperty("RequestedCardType")
    private String requestedCardType;

    @JsonProperty("RequestedElementType")
    private String requestedElementType;

    @JsonProperty("RequestedMinDamage")
    private int requestedMinDamage;

    @JsonProperty("UserId")
    private long userId;

    @JsonProperty("IsCompleted")
    private boolean isCompleted;

    public TradingDeal() {}

    public TradingDeal(String id, String offeredCardId, String requestedCardType, String requestedElementType, int requestedMinDamage, long userId, boolean isCompleted) {
        this.id = id;
        this.offeredCardId = offeredCardId;
        this.requestedCardType = requestedCardType;
        this.requestedElementType = requestedElementType;
        this.requestedMinDamage = requestedMinDamage;
        this.userId = userId;
        this.isCompleted = isCompleted;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOfferedCardId() { return offeredCardId; }
    public void setOfferedCardId(String offeredCardId) { this.offeredCardId = offeredCardId; }

    public String getRequestedCardType() { return requestedCardType; }
    public void setRequestedCardType(String requestedCardType) { this.requestedCardType = requestedCardType; }

    public String getRequestedElementType() { return requestedElementType; }
    public void setRequestedElementType(String requestedElementType) { this.requestedElementType = requestedElementType; }

    public int getRequestedMinDamage() { return requestedMinDamage; }
    public void setRequestedMinDamage(int requestedMinDamage) { this.requestedMinDamage = requestedMinDamage; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}