// (c)2016 Flipboard Inc, All Rights Reserved.

package com.example.kotlindemo.network.api;


import com.example.kotlindemo.model.ZhuangbiImage;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ZhuangbiApi {
    @GET("search")
    Observable<List<ZhuangbiImage>> search(@Query("q") String query);
}
