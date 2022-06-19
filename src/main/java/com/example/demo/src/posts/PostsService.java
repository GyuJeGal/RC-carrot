package com.example.demo.src.posts;

import com.example.demo.config.BaseException;
import com.example.demo.src.posts.model.GetPostRes;
import com.example.demo.src.posts.model.GetPostsRes;
import com.example.demo.src.sellPost.SellPostDao;
import com.example.demo.src.sellPost.model.GetSellPostsRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.FAILED_TO_SEARCH_POST;

@Service
@Transactional
public class PostsService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PostsDao postsDao;
    private final JwtService jwtService;

    @Autowired
    public PostsService(PostsDao postsDao, JwtService jwtService) {
        this.postsDao = postsDao;
        this.jwtService = jwtService;
    }


    public List<GetPostsRes> getPosts(int userId) throws BaseException {
        try {
            List <GetPostsRes> getPostsResList = postsDao.getPosts(userId);
            return getPostsResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public List<GetPostsRes> getPostsTopic(int userId, String topicName) throws BaseException {
        try {
            List <GetPostsRes> getPostsResList = postsDao.getPostsTopic(userId, topicName);
            return getPostsResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public GetPostRes getPost(int userId, int postId) throws BaseException {
        if(postsDao.checkPost(postId) == 0) {
            throw new BaseException(FAILED_TO_SEARCH_POST);
        }

        try {
            GetPostRes getPostRes = postsDao.getPost(userId, postId);
            return getPostRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
