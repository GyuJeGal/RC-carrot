package com.example.demo.src.sellPost.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class DeleteSellPost {
    private int userId;
    private int sellPostId;
}
