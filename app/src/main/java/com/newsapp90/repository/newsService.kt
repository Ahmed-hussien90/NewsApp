package com.newsapp90.repository

import com.newsapp90.model.NewsData
import com.newsapp90.util.constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface newsService {


    @GET("v2/top-headlines")
    suspend fun getTopNews(
        @Query("country") countryCode: String = "eg",
        @Query("apiKey") apiKey: String = constants.API_KEY
    ): Call<List<NewsData>>


    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") keyword: String = "apple",
        @Query("sortBy") sortBy: String = "popularity",
        @Query("apiKey") apiKey: String = constants.API_KEY
    ): Call<List<NewsData>>

}