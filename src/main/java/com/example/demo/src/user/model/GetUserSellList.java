package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class GetUserSellList {
    private int sellPostId;
    private int status;
    private String imgUrl;
    private String title;
    private String addressName;
    private int pullUpTime;
    private String uploadAt;
    private boolean isShare;
    private int price;
    private int countChatting;
    private int countLikes;
}
