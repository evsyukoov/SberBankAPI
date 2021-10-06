package com.evsyukoov.project.model.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RestContragent {

    @JsonProperty("name")
    private String name;

    @JsonProperty("bank")
    private String bank;

    @JsonProperty("account")
    private List<RestAccount> restAccount;

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

    public List<RestAccount> getRestAccount() {
        return restAccount;
    }

    public void setRestAccount(List<RestAccount> restAccount) {
        this.restAccount = restAccount;
    }
}
