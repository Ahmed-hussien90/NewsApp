package com.newsapp90.repository.service

import com.newsapp90.model.NewsData
import com.newsapp90.util.constants
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {


    @GET("v2/top-headlines")
    suspend fun getTopNews(
        @Query("country") countryCode: String = "us",
        @Query("page") pageNumber: Int,
        @Query("apiKey") apiKey: String = constants.API_KEY
    ): Response<NewsData>


    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") keyword: String = "apple",
        @Query("sortBy") sortBy: String = "popularity",
        @Query("apiKey") apiKey: String = constants.API_KEY
    ): Response<NewsData>

}