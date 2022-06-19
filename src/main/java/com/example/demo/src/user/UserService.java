package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final JwtService jwtService;

    @Autowired
    public UserService(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public void modifyUser(PatchUserReq patchUserReq) throws BaseException {

        //없는 유저일때
        if(userDao.isExistUser(patchUserReq.getUserId()) == 0) {
            throw new BaseException(FAILED_TO_SEARCH);
        }
        //있는 유저일때
        try {
                userDao.modifyUser(patchUserReq);

        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }


    }

    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {


        if(userDao.checkPhoneNumber(postUserReq.getPhoneNumber()) == 1) {
            throw new BaseException(DUPLICATED_PHONE_NUMBER);
        }

        try {
            int userId = userDao.createUser(postUserReq);
            String jwt = jwtService.createJwt(userId);
            return new PostUserRes(userId, jwt);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public GetUserRes getUser(int userId) throws BaseException {
        //없는 유저일때
        if(userDao.isExistUser(userId) == 0) {
            throw new BaseException(FAILED_TO_SEARCH);
        }
        //있는 유저일때
        try {
            return userDao.getUser(userId);

        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public void deleteUser(int userId) throws BaseException {
        //없는 유저일때
        if(userDao.isExistUser(userId) == 0) {
            throw new BaseException(FAILED_TO_SEARCH);
        }
        //있는 유저일때
        try {
            userDao.deleteUser(userId);
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserEmail(PatchUserEmail patchUserEmail) throws BaseException{
        //없는 유저일때
        if(userDao.isExistUser(patchUserEmail.getUserId()) == 0) {
            throw new BaseException(FAILED_TO_SEARCH);
        }
        //있는 유저일때
        try {
            userDao.modifyUserEmail(patchUserEmail);
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyPhoneNumber(PatchPhoneNumber patchPhoneNumber) throws BaseException {
        //없는 유저일때
        if(userDao.isExistUser(patchPhoneNumber.getUserId()) == 0) {
            throw new BaseException(FAILED_TO_SEARCH);
        }
        if(userDao.checkPhoneNumber(patchPhoneNumber.getPhoneNumber()) == 1) {
            throw new BaseException(DUPLICATED_PHONE_NUMBER);
        }

        //있는 유저일때
        try {
            userDao.modifyPhoneNumber(patchPhoneNumber);
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUsersBuyList> getBuyList(int userId) throws BaseException {
        try {
            return userDao.getBuyList(userId);
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserSellList> getSellList(int userId) throws BaseException {
        try {
            return userDao.getSellList(userId);
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserNotices> getUserNotices(int userId) throws BaseException {
        try {
            return userDao.getUserNotices(userId);
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetUserNotice getUserNotice(int userId, int noticeId) throws BaseException {
        try {
            return userDao.getUserNotice(userId, noticeId);
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserGathers> getUserGather(int userId) throws BaseException {
        try {
            return userDao.getUserGather(userId);
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public GetUserAccountList getUserAccountList(int userId, String searchYearMonth) throws BaseException {
        try {
            return userDao.getUserAccountList(userId, searchYearMonth);
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetInterestList> getInterestList(int userId) throws BaseException {
        try {
            return userDao.getInterestList(userId);
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
