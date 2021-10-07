package com.evsyukoov.project.model.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthRequestParams {
    @JsonProperty("name")
    private String name;

    @JsonProperty("pass")
    private String pass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
