package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class GetUserGathers {
    private int sellPostId;
    private int gatheredUserId;
    private String imgUrl;
    private String nickName;
    private String addressName;
    private int status;
    private String title;
    private int price;
    private int countChatting;
    private int countLikes;
}
