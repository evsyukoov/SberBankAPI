package com.evsyukoov.project.model.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddContragentRequestParams {

    @JsonProperty
    private String name;

    @JsonProperty
    private String bank;

    @JsonProperty
    private String accountNumber;

    @JsonProperty
    private String accountType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
