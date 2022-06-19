package com.example.demo.src.sellPost.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GetSellPostRes {

    public GetSellPostRes(int sellPostId, int userId, String nickName, String addressName, double mannerTemp,
                          String title, String categoryName, boolean pullUp, String uploadAt, int countChatting,
                          int countLikes, int countView, int price, boolean priceProposal) {
        this.sellPostId = sellPostId;
        this.userId = userId;
        this.nickName = nickName;
        this.addressName = addressName;
        this.mannerTemp = mannerTemp;
        this.title = title;
        this.categoryName = categoryName;
        this.pullUp = pullUp;
        this.uploadAt = uploadAt;
        this.countChatting = countChatting;
        this.countLikes = countLikes;
        this.countView = countView;
        this.price = price;
        this.priceProposal = priceProposal;
    }

    private int sellPostId;
    private int userId;
    private String nickName;
    private String addressName;
    private double mannerTemp;
    private String title;
    private String categoryName;
    private boolean pullUp;
    private String uploadAt;
    private int countChatting;
    private int countLikes;
    private int countView;
    private int price;
    private boolean priceProposal;
    private List<String> imgUrl;
}
