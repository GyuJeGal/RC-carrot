package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_ADDRESS(false, 2020, "주소를 입력해주세요."),
    POST_USERS_RANGE_ADDRESS(false, 2021, "주소 범위가 올바르지 않습니다."),
    POST_USERS_INVALID_ADDRESS(false, 2022, "올바르지 않은 주소입니다."),

    POST_USERS_EMPTY_PHONE_NUMBER(false, 2030, "휴대폰 번호를 입력해주세요."),
    POST_USERS_INVALID_PHONE_NUMBER(false, 2031, "휴대폰 번호를 다시 입력해주세요."),

    POST_USERS_EMPTY_NICKNAME(false, 2040, "닉네임을 입력해주세요."),
    POST_USERS_RANGE_NICKNAME(false, 2041, "닉네임은 최소 2자, 최대 12자로 입력해주세요."),
    POST_USERS_INVALID_NICKNAME(false, 2042, "닉네임은 띄어쓰기 없이 한글, 영문, 숫자만 가능해요."),

    POST_USERS_RANGE_IMG_URL(false, 2050, "이미지 URL은 최대 2048자까지 허용가능합니다."),

    // [PATCH] /users/{userId}
    PATCH_USERS_EMPTY_NICKNAME(false, 2050, "닉네임을 입력해주세요."),
    PATCH_USERS_RANGE_NICKNAME(false, 2051, "닉네임은 최소 2자, 최대 12자로 입력해주세요."),
    PATCH_USERS_INVALID_NICKNAME(false, 2052, "닉네임은 띄어쓰기 없이 한글, 영문, 숫자만 가능해요."),
    PATCH_USERS_RANGE_IMG_URL(false, 2060, "이미지 URL은 최대 2048자까지 허용가능합니다."),
    PATCH_EMPTY_VARIABLE(false, 2070, "닉네임과 이미지 URL 중 최소 하나를 입력해주세요"),

    // [PATCH] /users/{userId}/email
    PATCH_USERS_EMPTY_EMAIL(false, 2080, "email 주소를 입력해주세요."),
    PATCH_USERS_RANGE_EMAIL(false, 2081, "email 주소 범위가 올바르지 않습니다."),
    PATCH_USERS_INVALID_EMAIL(false, 2082, "올바르지 않은 email 주소입니다."),

    // [PATCH] /users/{userId}/phoneNumber
    PATCH_USERS_EMPTY_PHONE_NUMBER(false, 2090, "휴대폰 번호를 입력해주세요."),
    PATCH_USERS_INVALID_PHONE_NUMBER(false, 2091, "휴대폰 번호를 다시 입력해주세요."),

    // [GET] /users/{userId}/accountList
    GET_EMPTY_PARAMETER(false, 2100, "조회할 연도와 월을 입력해주세요."),

    // [POST] /sellPost/{userId}
    POST_SELLPOST_RANGE_IMG_URL(false, 2200, "이미지 URL은 최대 2048자까지 허용가능합니다."),

    POST_SELLPOST_EMPTY_TITLE(false, 2210, "제목을 입력해주세요."),
    POST_SELLPOST_RANGE_TITLE(false, 2211, "제목은 최소 2자, 최대 64자까지 허용가능합니다."),

    POST_SELLPOST_EMPTY_CATEGORY(false, 2220, "상품 카테고리를 입력해주세요."),
    POST_SELLPOST_RANGE_CATEGORY(false, 2221, "상품 카테고리를 다시 입력해주세요."),

    POST_SELLPOST_EMPTY_PRICE(false, 2230, "상품 가격을 입력해주세요."),
    POST_SELLPOST_RANGE_PRICE(false, 2231, "상품 가격은 1,000,000,000까지 허용가능합니다."),

    POST_SELLPOST_EMPTY_CONTENTS(false, 2240, "게시글 내용을 입력해주세요."),
    POST_SELLPOST_RANGE_CONTENTS(false, 2241, "게시글 내용은 최대 1000자까지 입력해주세요."),
    
    POST_SELLPOST_EMPTY_ADDRESS(false, 2250, "판매할 주소를 입력해주세요."),
    POST_SELLPOST_RANGE_ADDRESS(false, 2251, "판매할 주소를 다시 입력해주세요."),
    POST_SELLPOST_INVALID_ADDRESS(false, 2252, "올바르지 않은 주소입니다."),


    //[Get] /posts/:userId?topicName=
    GET_POSTS_RANGE_TOPICNAME(false, 2260, "주제를 다시 입력해주세요."),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    DUPLICATED_PHONE_NUMBER(false, 3020, "중복된 휴대폰 번호입니다."),
    FAILED_TO_SEARCH(false,3030,"없는 사용자입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),


    FAILED_TO_SEARCH_SELLPOST(false,3040,"없는 판매 게시글입니다."),


    //[POST] /sellPost/{userId}
    INVALID_CATEGORY(false,3050,"잘못된 카테고리 ID 입니다."),


    //[PATCH] /sellPost/{userId}/delete
    FAILED_DELETE_SELLPOST(false,3060,"게시글을 삭제할 수 없습니다."),

    FAILED_TO_SEARCH_POST(false,3070,"없는 동네 생활 게시글입니다."),
    
    LOGIN_FAILED(false,3080,"로그인에 실패하였습니다."),

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
