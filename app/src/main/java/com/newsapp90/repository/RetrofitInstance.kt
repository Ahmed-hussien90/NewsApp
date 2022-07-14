package com.newsapp90.repository

import com.newsapp90.util.constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance() {


    companion object {

        private val retrofitClient by lazy {
            Retrofit.Builder()
                .baseUrl(constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(newsService::class.java)
        }

    }
}