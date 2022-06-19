package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class GetInterestList {
    private int sellPostId;
    private String imgUrl;
    private String title;
    private String addressName;
    private boolean isShare;
    private int price;
    private int countChatting;
    private int countLikes;
}
