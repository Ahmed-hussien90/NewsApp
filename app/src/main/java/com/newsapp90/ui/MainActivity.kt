package com.newsapp90.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.newsapp90.R
import com.newsapp90.viewmodel.NewsViewModel

class MainActivity : AppCompatActivity() {

    lateinit var newsViewModel: NewsViewModel

    lateinit var newsRecycler: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        newsRecycler = findViewById(R.id.news_recycler)

        newsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        newsViewModel.mutableLiveData.observe(this) { t -> print(t.totalResults.toString()) }



        newsViewModel.getNews()


    }
}