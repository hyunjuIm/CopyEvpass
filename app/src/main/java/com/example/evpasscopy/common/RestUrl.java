package com.example.evpasscopy.common;

public class RestUrl {
    public static final String URL_API                      = "http://1.245.175.56:60012";

    //정책
    public static final String URL_API_POLICY               = URL_API + "/v1/app/policy";
    //SMS 인증 요청
    public static final String URL_API_AUTH_REQUEST         = URL_API + "/v1/user/auth/request";
    //SMS 인증 확인
    public static final String URL_API_AUTH_CONFIRM         = URL_API + "/v1/user/auth/confirm";
    //회원가입
    public static final String URL_API_USER_JOIN            = URL_API + "/v1/user/join";
    //로그인  /토큰필요/
    public static final String URL_API_USER_LOGIN           = URL_API + "/v1/user/login";
    //대여소 목록
    public static final String URL_API_STATION_LIST         = URL_API + "/v1/station";
}
