package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserRes {
    private int userId;
    private String nickName;
    private double mannerTemp;
    private double reTradeRate;
    private double responseRate;
    private String addressName1;
    private String addressName2;
    private Integer certifyTime1;
    private Integer certifyTime2;
    private String lastActive;
    private String createAt;
    private int countGetBadge;
    private int countSellPost;

    public GetUserRes(int userId, String nickName, double mannerTemp, double reTradeRate, double responseRate, String lastActive, String createAt, int countGetBadge, int countSellPost) {
        this.userId = userId;
        this.nickName = nickName;
        this.mannerTemp = mannerTemp;
        this.reTradeRate = reTradeRate;
        this.responseRate = responseRate;
        this.lastActive = lastActive;
        this.createAt = createAt;
        this.countGetBadge = countGetBadge;
        this.countSellPost = countSellPost;
    }
}
