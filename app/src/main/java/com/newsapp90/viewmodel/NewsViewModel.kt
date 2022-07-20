package com.newsapp90.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.newsapp90.model.NewsData
import com.newsapp90.repository.service.RetrofitInstance
import com.newsapp90.util.constants.Companion.API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {

    var mutableLiveData: MutableLiveData<NewsData> = MutableLiveData()

    fun getNews() {

        GlobalScope.launch(Dispatchers.IO) {
            var response = RetrofitInstance.api.getTopNews("us", 1, API_KEY)
            if (response.isSuccessful) {
                mutableLiveData.postValue(response.body())
            }
        }


    }

}