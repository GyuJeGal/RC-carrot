package com.example.demo.src.posts.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GetPostRes {
    public GetPostRes(String topicName, int userId, String profileImgUrl, String nickName, String addressName,
                      int certifyTime, String uploadAt, String contents, int countEmpathy, int countComment) {
        this.topicName = topicName;
        this.userId = userId;
        this.profileImgUrl = profileImgUrl;
        this.nickName = nickName;
        this.addressName = addressName;
        this.certifyTime = certifyTime;
        this.uploadAt = uploadAt;
        this.contents = contents;
        this.countEmpathy = countEmpathy;
        this.countComment = countComment;
    }

    private String topicName;
    private int userId;
    private String profileImgUrl;
    private String nickName;
    private String addressName;
    private int certifyTime;
    private String uploadAt;
    private String contents;
    private List<String> imgUrlList;
    private int countEmpathy;
    private int countComment;
    private List<CommentStruct> commentStructList;
}
