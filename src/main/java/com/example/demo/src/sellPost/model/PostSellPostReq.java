package com.example.demo.src.sellPost.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class PostSellPostReq {
    private List<String> imgUrlList;
    private String title;
    private Integer categoryId;
    private Integer price;
    private Integer priceProposal;
    private String contents;
    private String addressName;
}
