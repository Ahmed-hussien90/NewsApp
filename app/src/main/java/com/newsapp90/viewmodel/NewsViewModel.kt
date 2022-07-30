package com.newsapp90.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newsapp90.model.Article
import com.newsapp90.model.NewsData
import com.newsapp90.repository.database.NewsDataBase
import com.newsapp90.repository.NewsRepository
import com.newsapp90.repository.service.RetrofitInstance
import com.newsapp90.util.Constants.Companion.API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NewsViewModel(val newsRepository: NewsRepository) : ViewModel() {

    var mutableLiveData: MutableLiveData<NewsData> = MutableLiveData()
    var articleMutableLiveData: MutableLiveData<MutableList<Article>> = MutableLiveData()

    //from Apis
    fun getNews(
        countryCode: String?,
        pageNumber: Int,
        category: String?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            mutableLiveData.postValue(newsRepository.getNews(countryCode, pageNumber, category))
        }
    }

    fun searchNews(keyword: String?, language: String?, sortedBy: String, maxResults: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mutableLiveData.postValue(
                newsRepository.searchNews(
                    keyword,
                    language,
                    sortedBy,
                    maxResults
                )
            )
        }
    }

    //dataBase
    fun insertArticle(context: Context, article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.insertArticle(article)
        }
    }

    fun getSavedArticle() {
        viewModelScope.launch(Dispatchers.IO) {
            articleMutableLiveData.postValue(newsRepository.getSavedArticle())
        }
    }

    fun deleteArticle(articleUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.deleteArticle(articleUrl)
        }
    }
}