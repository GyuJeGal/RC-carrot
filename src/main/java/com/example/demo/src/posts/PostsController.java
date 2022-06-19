package com.example.demo.src.posts;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.posts.model.GetPostRes;
import com.example.demo.src.posts.model.GetPostsRes;
import com.example.demo.src.sellPost.model.GetSellPostsRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.GET_POSTS_RANGE_TOPICNAME;

@RestController
@RequestMapping("/posts")
public class PostsController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PostsService postsService;
    private final JwtService jwtService;

    @Autowired
    public PostsController(PostsService postsService, JwtService jwtService) {
        this.postsService = postsService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<List<GetPostsRes>> getPosts(@PathVariable int userId, @RequestParam(value = "topicName", required = false) String topicName) {

        try {
            if(topicName == null) {
                List<GetPostsRes> getPostsResList = postsService.getPosts(userId);
                return new BaseResponse<>(getPostsResList);
            }
            else {
                if(topicName.length() > 20) {
                    return new BaseResponse<>(GET_POSTS_RANGE_TOPICNAME);
                }
                List<GetPostsRes> getPostsResList = postsService.getPostsTopic(userId, topicName);
                return new BaseResponse<>(getPostsResList);
            }

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/{userId}/{postId}")
    public BaseResponse<GetPostRes> getPost(@PathVariable("userId") int userId, @PathVariable("postId") int postId) {
        try {
            GetPostRes getPostRes = postsService.getPost(userId, postId);
            return new BaseResponse<>(getPostRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


}
