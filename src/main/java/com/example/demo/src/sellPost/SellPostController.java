package com.example.demo.src.sellPost;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.sellPost.model.DeleteSellPost;
import com.example.demo.src.sellPost.model.GetSellPostRes;
import com.example.demo.src.sellPost.model.GetSellPostsRes;
import com.example.demo.src.sellPost.model.PostSellPostReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Pattern;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/sellPosts")
public class SellPostController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SellPostService sellPostService;
    private final JwtService jwtService;

    @Autowired
    public SellPostController(SellPostService sellPostService, JwtService jwtService) {
        this.sellPostService = sellPostService;
        this.jwtService = jwtService;
    }

    //홈화면, 게시글 다 보여줌
    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<List<GetSellPostsRes>> getSellPosts(@PathVariable("userId") int userId) {
        try {
            int userIdByJwt = jwtService.getUserIdx();
            if(userIdByJwt != userId) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetSellPostsRes> getSellPostsRes = sellPostService.getSellPosts(userId);
            return new BaseResponse<>(getSellPostsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //특정 게시글 조회
    @ResponseBody
    @GetMapping("/{userId}/{sellPostId}")
    public BaseResponse<GetSellPostRes> getSellPost(@PathVariable("userId") int userId, @PathVariable("sellPostId") int sellPostId) {
        try {
            int userIdByJwt = jwtService.getUserIdx();
            if(userIdByJwt != userId) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            GetSellPostRes getSellPostRes = sellPostService.getSellPost(userId, sellPostId);
            return new BaseResponse<>(getSellPostRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/{userId}")
    public BaseResponse<String> createSellPost(@PathVariable int userId, @RequestBody PostSellPostReq postSellPostReq) {

        //이미지 URL 형식적 Validation
        if(postSellPostReq.getImgUrlList() != null) {
            for (String checkString : postSellPostReq.getImgUrlList()) {
                if(checkString.length() > 2048) {
                    return new BaseResponse<>(POST_SELLPOST_RANGE_IMG_URL);
                }
            }
        }

        //제목 형식적 Validation
        if(postSellPostReq.getTitle() == null) {
            return new BaseResponse<>(POST_SELLPOST_EMPTY_TITLE);
        }
        
        if(postSellPostReq.getTitle().length() > 64 || postSellPostReq.getTitle().length() < 2) {
            return new BaseResponse<>(POST_SELLPOST_RANGE_TITLE);
        }

        //카테고리 형식적 Validation
        if(postSellPostReq.getCategoryId() == null) {
            return new BaseResponse<>(POST_SELLPOST_EMPTY_CATEGORY);
        }

        if(postSellPostReq.getCategoryId() > 100 || postSellPostReq.getCategoryId() < 0) {
            return new BaseResponse<>(POST_SELLPOST_RANGE_CATEGORY);
        }

        //가격 형식적 Validation
        if(postSellPostReq.getPrice() == null) {
            return new BaseResponse<>(POST_SELLPOST_EMPTY_PRICE);
        }
        
        if(postSellPostReq.getPrice() > 1000000000 || postSellPostReq.getPrice() < 0) {
            return new BaseResponse<>(POST_SELLPOST_RANGE_PRICE);
        }

        //내용 형식적 Validation
        if(postSellPostReq.getContents() == null) {
            return new BaseResponse<>(POST_SELLPOST_EMPTY_CONTENTS);
        }

        if(postSellPostReq.getContents().length() > 1000) {
            return new BaseResponse<>(POST_SELLPOST_RANGE_CONTENTS);
        }

        //주소 형식적 Validation
        if(postSellPostReq.getAddressName() == null) {
            return new BaseResponse<>(POST_SELLPOST_EMPTY_ADDRESS);
        }

        if(postSellPostReq.getAddressName().length() < 2 || postSellPostReq.getAddressName().length() > 100) {
            return new BaseResponse<>(POST_SELLPOST_RANGE_ADDRESS);
        }

        //주소 형식은 "용현동, 용현1,4동, 면목제3.8동", 이 형식을 제외한 나머지는 오류 처리
        String addressPattern = "^[([가-힣]+([0-9]|[0-9](,|.)[0-9])+(읍|면|동))|([가-힣])|]*$";
        if(!Pattern.matches(addressPattern, postSellPostReq.getAddressName())) {
            return new BaseResponse<>(POST_SELLPOST_INVALID_ADDRESS);
        }

        try {
            int userIdByJwt = jwtService.getUserIdx();
            if(userIdByJwt != userId) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            sellPostService.createSellPost(userId, postSellPostReq);
            String result = "게시글 생성을 완료했습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/{userId}/delete")
    public BaseResponse<String> deleteSellPost(@PathVariable int userId, @RequestBody DeleteSellPost deleteSellPost) {
        try {
            int userIdByJwt = jwtService.getUserIdx();
            if(userIdByJwt != userId) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            deleteSellPost.setUserId(userId);
            sellPostService.deleteSellPost(deleteSellPost);
            String result = "게시글 삭제를 완료했습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }



}
