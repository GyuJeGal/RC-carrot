package com.example.demo.src.posts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CommentStruct {
    private int commentId;
    private String profileImgUrl;
    private String nickName;
    private String addressName;
    private String createAt;
    private String contents;
    private int parentIdx;
    private int countLikes;
    private String imgUrl;
}
