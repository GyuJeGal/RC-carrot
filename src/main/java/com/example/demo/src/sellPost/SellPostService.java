package com.example.demo.src.sellPost;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.sellPost.model.DeleteSellPost;
import com.example.demo.src.sellPost.model.GetSellPostRes;
import com.example.demo.src.sellPost.model.GetSellPostsRes;
import com.example.demo.src.sellPost.model.PostSellPostReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@Transactional
public class SellPostService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SellPostDao sellPostDao;
    private final JwtService jwtService;

    @Autowired
    public SellPostService(SellPostDao sellPostDao, JwtService jwtService) {
        this.sellPostDao = sellPostDao;
        this.jwtService = jwtService;
    }

    public List<GetSellPostsRes> getSellPosts(int userId) throws BaseException {
        //없는 사용자일때
        if(sellPostDao.isExistsUser(userId) == 0) {
            throw new BaseException(FAILED_TO_SEARCH);
        }
        //있는 사용자일때
        try {
            List <GetSellPostsRes> getSellPostsRes = sellPostDao.getSellPosts(userId);
            return getSellPostsRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }


    }

    public GetSellPostRes getSellPost(int userId, int sellPostId) throws BaseException {
        if(sellPostDao.isExistsSellPost(sellPostId) == 0) {
            throw new BaseException(FAILED_TO_SEARCH_SELLPOST);
        }

        try {
            return sellPostDao.getSellPost(userId, sellPostId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public void createSellPost(int userId, PostSellPostReq postSellPostReq) throws BaseException {
        if(sellPostDao.checkCategoryId(postSellPostReq.getCategoryId()) == 0) {
            throw new BaseException(INVALID_CATEGORY);
        }

        try {
            sellPostDao.createSellPost(userId, postSellPostReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteSellPost(DeleteSellPost deleteSellPost) throws BaseException {
        if(sellPostDao.isExistsSellPost(deleteSellPost.getSellPostId()) == 0) {
            throw new BaseException(FAILED_TO_SEARCH_SELLPOST);
        }

        if(sellPostDao.checkSellPost(deleteSellPost) == 0) {
            throw new BaseException(FAILED_DELETE_SELLPOST);
        }

        try {
            sellPostDao.deleteSellPost(deleteSellPost);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
