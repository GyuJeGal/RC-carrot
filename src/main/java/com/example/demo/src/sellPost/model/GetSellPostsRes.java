package com.example.demo.src.sellPost.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class GetSellPostsRes {
    private int sellPostId;
    private String title;
    private String addressName;
    private int price;
    private String imgUrl;
    private boolean pullUp;
    private String uploadAt;
    private int countChatting;
    private int countLikes;

}
