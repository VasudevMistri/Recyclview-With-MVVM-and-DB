package com.app.wamatask.networks

import com.app.wamatask.restApi.model.NewsResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {

    @GET("top-headlines")
    fun getNewsList(
        @Query("country") param: String?,
        @Query("category") param1: String?,
        @Query("apiKey") param2: String?
    ): Call<NewsResponseModel>?

}