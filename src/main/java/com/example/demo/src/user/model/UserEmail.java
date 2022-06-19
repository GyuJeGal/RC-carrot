package com.example.demo.src.user.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserEmail {

    @JsonCreator
    public UserEmail(String email) {
        this.email = email;
    }

    private String email;
}
