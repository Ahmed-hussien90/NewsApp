package com.newsapp90.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.newsapp90.R
import com.newsapp90.ui.NewsAdapter
import com.newsapp90.viewmodel.NewsViewModel


class DiscoverFragment : Fragment() {

    lateinit var newsViewModel: NewsViewModel
    lateinit var newsRecycler: RecyclerView
    lateinit var topNewsRecycler: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_discover, container, false)
        initViews(view)
        loadNews()
        return view
    }

    fun initViews(view: View) {
        newsRecycler = view.findViewById(R.id.news_recycler)
        topNewsRecycler = view.findViewById(R.id.topnews_recycler)


    }

    fun loadNews() {

        var newsAdapter = this.context?.let { NewsAdapter(it, R.layout.news_list_item) }
        var topnewsAdapter =
            this.context?.let { NewsAdapter(it, R.layout.news_list_item_horizental) }

        newsRecycler.adapter = newsAdapter
        topNewsRecycler.adapter = topnewsAdapter

        newsRecycler.layoutManager =
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        topNewsRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        newsRecycler.isNestedScrollingEnabled = false;
        newsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        newsViewModel.mutableLiveData.observe(viewLifecycleOwner) { t ->
            if (newsAdapter != null) {
                newsAdapter.setlist(t.articles)
                topnewsAdapter?.setlist(t.articles)
            }
        }

        newsViewModel.getNews()
    }

}