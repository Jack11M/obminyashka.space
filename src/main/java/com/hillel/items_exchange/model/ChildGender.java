package com.hillel.items_exchange.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ChildGender {
    @JsonProperty("male")
    MALE("male"),
    @JsonProperty("female")
    FEMALE("female"),
    @JsonProperty("unselected")
    UNSELECTED("unselected");

    private final String gender;

    ChildGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }
}
