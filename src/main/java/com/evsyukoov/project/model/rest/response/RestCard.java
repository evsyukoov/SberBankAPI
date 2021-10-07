package com.evsyukoov.project.model.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class RestCard {

    @JsonProperty("number")
    private String number;

    @JsonProperty("balance")
    private BigDecimal balance;

    @JsonProperty("type")
    private String accountType;

    @JsonProperty("expireDate")
    private String expireDate;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }
}
