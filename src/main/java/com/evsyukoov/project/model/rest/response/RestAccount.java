package com.evsyukoov.project.model.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RestAccount {

    @JsonProperty
    private String number;

    @JsonProperty
    private String accountType;

    @JsonProperty
    private List<RestCard> cards;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public List<RestCard> getCards() {
        return cards;
    }

    public void setCards(List<RestCard> cards) {
        this.cards = cards;
    }
}
