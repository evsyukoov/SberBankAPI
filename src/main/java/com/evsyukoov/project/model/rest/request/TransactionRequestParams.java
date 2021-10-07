package com.evsyukoov.project.model.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class TransactionRequestParams {

    @JsonProperty
    private String cardNumberFrom;

    @JsonProperty
    private String cardNumberTo;

    @JsonProperty
    private BigDecimal money;

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getCardNumberFrom() {
        return cardNumberFrom;
    }

    public void setCardNumberFrom(String cardNumberFrom) {
        this.cardNumberFrom = cardNumberFrom;
    }

    public String getCardNumberTo() {
        return cardNumberTo;
    }

    public void setCardNumberTo(String cardNumberTo) {
        this.cardNumberTo = cardNumberTo;
    }
}
