package com.example.kakaopay.util;

public class ApiUtils {
    public static final String BASE_URL
            ="https://dapi.kakao.com/v2/search/";
    public static ImageApiInterface getImageApi(){
        return ImageUtil.getRetrofit(BASE_URL).create(ImageApiInterface.class);
    }
}
