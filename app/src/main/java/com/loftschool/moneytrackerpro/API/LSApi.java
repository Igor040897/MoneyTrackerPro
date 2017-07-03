package com.loftschool.moneytrackerpro.API;

import com.loftschool.moneytrackerpro.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by fanre on 6/29/2017.
 */

public interface LSApi {

    @Headers("Content-Type: application/json")
    @GET("items")
    Call<List<Item>> items(@Query("type") String type);

    @Headers("Content-Type: application/json")
    @POST("items/add")
    Call<AddResult> add(@Query("name") String name, @Query("price") int price, @Query("type") String type);

    @Headers("Content-Type: application/json")
    @POST("items/remove")
    Call<Result> remove(@Query("id") int id);
}
