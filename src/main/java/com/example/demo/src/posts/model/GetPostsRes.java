package com.example.demo.src.posts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GetPostsRes {

    public GetPostsRes(int postId, String topicName, String contents, String nickName, String addressName,
                       String uploadAt, int countEmpathy, int countComment) {
        this.postId = postId;
        this.topicName = topicName;
        this.contents = contents;
        this.nickName = nickName;
        this.addressName = addressName;
        this.uploadAt = uploadAt;
        this.countEmpathy = countEmpathy;
        this.countComment = countComment;
    }

    private int postId;
    private String topicName;
    private List<String> imgUrlList;
    private String contents;
    private String nickName;
    private String addressName;
    private String uploadAt;
    private int countEmpathy;
    private int countComment;
}
