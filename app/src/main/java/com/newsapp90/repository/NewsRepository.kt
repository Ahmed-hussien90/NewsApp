package com.newsapp90.repository

import android.content.res.Resources
import android.widget.Toast
import com.newsapp90.R
import com.newsapp90.model.Article
import com.newsapp90.model.NewsData
import com.newsapp90.repository.database.NewsDataBase
import com.newsapp90.repository.service.RetrofitInstance
import com.newsapp90.util.Constants
import com.newsapp90.util.isOnline


class NewsRepository(val db: NewsDataBase) {

    //from Apis
    suspend fun getNews(countryCode: String?, pageNumber: Int, category: String?): NewsData? {
        var response =
            RetrofitInstance.api.getTopNews(
                countryCode, pageNumber, category,
                Constants.API_KEY
            )
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun searchNews(
        keyword: String?,
        language: String?,
        sortedBy: String,
        maxResults: String
    ): NewsData? {
        var response =
            RetrofitInstance.api.searchNews(
                keyword, sortedBy,
                Constants.API_KEY, language, maxResults
            )
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

//dataBase

    fun insertArticle(article: Article) {
        db.NewsDao().insert(article)
    }

    fun getSavedArticle(): MutableList<Article> {
        return db.NewsDao().getArticles()
    }

    fun deleteArticle(articleUrl: String) {
        db.NewsDao().deleteArticle(articleUrl)
    }
}