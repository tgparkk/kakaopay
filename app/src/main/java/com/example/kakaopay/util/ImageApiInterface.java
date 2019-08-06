package com.example.kakaopay.util;
import com.example.kakaopay.model.ImageInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ImageApiInterface {

    @Headers({"Authorization: KakaoAK 34fc03ea11af92d26ab09fee9675b97f"})
    @GET("image.json?")
    Call<ImageInfo>getImageItemsMores(@Query("query")String imageName,
                                      @Query("page")int size);


    @Headers({"Authorization: KakaoAK 34fc03ea11af92d26ab09fee9675b97f"})
    @GET("image.json?")
    Call<ImageInfo>getImageItemsLoad(@Query("query")String imageName);
}
