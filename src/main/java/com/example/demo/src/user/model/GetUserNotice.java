package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GetUserNotice {
    public GetUserNotice(int noticeId, String title, String createAt, String contents) {
        this.noticeId = noticeId;
        this.title = title;
        this.createAt = createAt;
        this.contents = contents;
    }

    private int noticeId;
    private String title;
    private String createAt;
    private String contents;
    private List<String> imgUrls;
}
