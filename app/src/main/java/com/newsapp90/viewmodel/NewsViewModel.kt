package com.newsapp90.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newsapp90.model.NewsData

class NewsViewModel : ViewModel() {
    var mutableLiveData: MutableLiveData<List<NewsData>> = MutableLiveData()


    fun getNews() {

    }

}