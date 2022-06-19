package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Pattern;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/users")
public class UserController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLogInReq postLogInReq) {

        String phoneNumberPattern = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$";
        if(!Pattern.matches(phoneNumberPattern, postLogInReq.getPhoneNumber())) {
            return new BaseResponse<>(POST_USERS_INVALID_PHONE_NUMBER);
        }

        try {
            PostLoginRes postLoginRes = userService.logIn(postLogInReq.getPhoneNumber());
            return new BaseResponse<>(postLoginRes);
        }catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    @PostMapping("")
    @ResponseBody
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        //주소 형식적 Validation
        if(postUserReq.getAddressName() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_ADDRESS);
        }
        if(postUserReq.getAddressName().length() < 2 || postUserReq.getAddressName().length() > 100) {
            return new BaseResponse<>(POST_USERS_RANGE_ADDRESS);
        }
        //주소 형식은 "용현동, 용현1,4동, 면목제3.8동", 이 형식을 제외한 나머지는 오류 처리
        String addressPattern = "^[([가-힣]+([0-9]|[0-9](,|.)[0-9])+(읍|면|동))|([가-힣])|]*$";
        if(!Pattern.matches(addressPattern, postUserReq.getAddressName())) {
            return new BaseResponse<>(POST_USERS_INVALID_ADDRESS);
        }

        //휴대폰 번호 형식적 Validation
        if(postUserReq.getPhoneNumber() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_PHONE_NUMBER);
        }

        String phoneNumberPattern = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$";
        if(!Pattern.matches(phoneNumberPattern, postUserReq.getPhoneNumber())) {
            return new BaseResponse<>(POST_USERS_INVALID_PHONE_NUMBER);
        }

        //닉네임 형식적 Validation
        if(postUserReq.getNickName() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_NICKNAME);
        }
        if(postUserReq.getNickName().length() < 2 || postUserReq.getNickName().length() > 13) {
            return new BaseResponse<>(POST_USERS_RANGE_NICKNAME);
        }

        String nickNamePattern = "^[0-9|a-z|A-Z|가-힣]*$"; //"ㄷㄷㄹ", "ㅏㅣㅑ", "!@#", "(띄어쓰기)" 다 안됨.
        if(!Pattern.matches(nickNamePattern, postUserReq.getNickName())) {
            return new BaseResponse<>(POST_USERS_INVALID_NICKNAME);
        }

        //프로필 사진 형식적 Validation
        if(postUserReq.getProfileImg() != null) {
            if(postUserReq.getProfileImg().length() > 2048) {
                return new BaseResponse<>(POST_USERS_RANGE_IMG_URL);
            }
        }

        System.out.println(postUserReq.getClass());

        try {
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch (BaseException baseException) {
            return new BaseResponse<>(baseException.getStatus());
        }

    }

    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<GetUserRes> getUser(@PathVariable("userId") int userId) {

        try {
            GetUserRes getUserRes = userService.getUser(userId);
            return new BaseResponse<>(getUserRes);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/{userId}")
    public BaseResponse<String> modifyUser(@PathVariable("userId") int userId, @RequestBody User user) {

        try {
            int userIdByJwt = jwtService.getUserIdx();
            if(userIdByJwt != userId) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

        if(user.getNickName() == null) {
            return new BaseResponse<>(PATCH_USERS_EMPTY_NICKNAME);
        }

        if(user.getProfileImg() != null) {
            if(user.getProfileImg().length() > 2048) {
                return new BaseResponse<>(PATCH_USERS_RANGE_IMG_URL);
            }
        }

        if(user.getNickName().length() < 2 || user.getNickName().length() > 13) {
            return new BaseResponse<>(PATCH_USERS_RANGE_NICKNAME);
        }

        String nickNamePattern = "^[0-9|a-z|A-Z|가-힣]*$"; //"ㄷㄷㄹ", "ㅏㅣㅑ", "!@#", "(띄어쓰기)" 다 안됨.
        if (!Pattern.matches(nickNamePattern, user.getNickName())) {
            return new BaseResponse<>(PATCH_USERS_INVALID_NICKNAME);
        }


        try {
            PatchUserReq patchUserReq = new PatchUserReq(userId, user.getProfileImg(), user.getNickName());
            userService.modifyUser(patchUserReq);

            String result = "사용자 프로필 수정 완료했습니다.";

            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }


    }

    @ResponseBody
    @PatchMapping("/{userId}/delete")
    public BaseResponse<String> deleteUser(@PathVariable("userId") int userId) {
        try {
            int userIdByJwt = jwtService.getUserIdx();
            if(userIdByJwt != userId) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            userService.deleteUser(userId);
            String result = "사용자 삭제 완료했습니다.";

            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/{userId}/email")
    public BaseResponse<String> modifyUserEmail(@PathVariable("userId") int userId, @RequestBody UserEmail userEmail) {

        if(userEmail.getEmail() == null) {
            return new BaseResponse<>(PATCH_USERS_EMPTY_EMAIL);
        }

        if(userEmail.getEmail().length() > 320) {
            return new BaseResponse<>(PATCH_USERS_RANGE_EMAIL);
        }

        String emailPattern = "^[([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})]$";
        if(Pattern.matches(emailPattern, userEmail.getEmail())) {
            return new BaseResponse<>(PATCH_USERS_INVALID_EMAIL);
        }



        try {
            int userIdByJwt = jwtService.getUserIdx();
            if(userIdByJwt != userId) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PatchUserEmail patchUserEmail = new PatchUserEmail(userId, userEmail.getEmail());
            userService.modifyUserEmail(patchUserEmail);
            String result = "사용자 e-mail 등록 완료했습니다.";

            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/{userId}/phoneNumber")
    public BaseResponse<String> modifyPhoneNumber(@PathVariable("userId") int userId, @RequestBody PhoneNumber phoneNumber) {

        if(phoneNumber == null) {
            return new BaseResponse<>(PATCH_USERS_EMPTY_PHONE_NUMBER);
        }

        String phoneNumberPattern = "^[[01]+([0]|[1]|[6-9])+[-]+([0-9]{3}|[0-9]{4})+[-]+[0-9]{4}]*$";
        if(!Pattern.matches(phoneNumberPattern, phoneNumber.getPhoneNumber())) {
            return new BaseResponse<>(PATCH_USERS_INVALID_PHONE_NUMBER);
        }

        try {
            int userIdByJwt = jwtService.getUserIdx();
            if(userIdByJwt != userId) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PatchPhoneNumber patchPhoneNumber = new PatchPhoneNumber(userId, phoneNumber.getPhoneNumber());
            userService.modifyPhoneNumber(patchPhoneNumber);
            String result = "사용자 휴대폰 번호 변경 완료했습니다.";

            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/{userId}/buyList")
    public BaseResponse<List<GetUsersBuyList>> getBuyList(@PathVariable("userId") int userId) {
        try {
            int userIdByJwt = jwtService.getUserIdx();
            if(userIdByJwt != userId) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetUsersBuyList> getUsersBuyList = userService.getBuyList(userId);
            return new BaseResponse<>(getUsersBuyList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/{userId}/sellList")
    public BaseResponse<List<GetUserSellList>> getSellList(@PathVariable("userId") int userId)  {
        try {
            int userIdByJwt = jwtService.getUserIdx();
            if(userIdByJwt != userId) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetUserSellList> getUserSellLists = userService.getSellList(userId);
            return new BaseResponse<>(getUserSellLists);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/{userId}/notices")
    public BaseResponse<List<GetUserNotices>> getNotices(@PathVariable int userId) {
        try {
            int userIdByJwt = jwtService.getUserIdx();
            if(userIdByJwt != userId) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            return new BaseResponse<>(userService.getUserNotices(userId));

        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/{userId}/{noticeId}")
    public BaseResponse<GetUserNotice> getNotice(@PathVariable("userId") int userId, @PathVariable("noticeId") int noticeId) {
        try {
            int userIdByJwt = jwtService.getUserIdx();
            if(userIdByJwt != userId) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            return new BaseResponse<>(userService.getUserNotice(userId, noticeId));

        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/{userId}/gather")
    public BaseResponse<List<GetUserGathers>> getUserGather(@PathVariable int userId) {
        try {
            int userIdByJwt = jwtService.getUserIdx();
            if(userIdByJwt != userId) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetUserGathers> getUserGathers = userService.getUserGather(userId);
            return new BaseResponse<>(getUserGathers);

        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/{userId}/accountList")
    public BaseResponse<GetUserAccountList> getUserAccountList(@PathVariable int userId, @RequestParam(value = "when", defaultValue = "2022-06") String searchYearMonth) {

        if(searchYearMonth == null) {
            return new BaseResponse<>(GET_EMPTY_PARAMETER);
        }

        try {
            int userIdByJwt = jwtService.getUserIdx();
            if(userIdByJwt != userId) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            return new BaseResponse<>(userService.getUserAccountList(userId, searchYearMonth));

        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/{userId}/interestList")
    public BaseResponse<List<GetInterestList>> getInterestList(@PathVariable int userId) {
        try {
            int userIdByJwt = jwtService.getUserIdx();
            if(userIdByJwt != userId) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetInterestList> getInterestLists = userService.getInterestList(userId);
            return new BaseResponse<>(getInterestLists);
        } catch(BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }




}
