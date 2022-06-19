package com.example.demo.src.user.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostLogInReq {

    @JsonCreator
    public PostLogInReq(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private String phoneNumber;
}
