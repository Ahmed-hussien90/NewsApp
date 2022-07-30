package com.newsapp90.ui.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsapp90.R
import com.newsapp90.databinding.FragmentNewsBinding
import com.newsapp90.ui.MainActivity
import com.newsapp90.ui.NewsAdapter
import com.newsapp90.util.isOnline
import com.newsapp90.util.shareNews
import com.newsapp90.viewmodel.NewsViewModel
import kotlinx.coroutines.*

class NewsFragment : Fragment() {

    lateinit var fragmentName: String
    lateinit var category: String
    lateinit var binding: FragmentNewsBinding
    var pageNo: Int = 0
    lateinit var newsViewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shimmerLayout.startShimmer()
        fragmentName =
            arguments?.getString("fragmentName", resources.getString(R.string.discover)).toString()
        category = arguments?.getString("category", "general").toString()
        pageNo = arguments?.getInt("sortedBy", 0) ?: 0
        loadNews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun loadNews() {
        newsViewModel = (activity as MainActivity).newsViewModel
        newsAdapter = NewsAdapter()
        binding.newsFragmentRecycler.layoutManager = LinearLayoutManager(context)
        binding.newsFragmentRecycler.adapter = newsAdapter

        newsViewModel.mutableLiveData.observe(viewLifecycleOwner) { t ->
            binding.shimmerLayout.stopShimmer()
            binding.shimmerLayout.visibility = View.GONE
            binding.newsFragmentRecycler.visibility = View.VISIBLE
            GlobalScope.launch(Dispatchers.IO) {
                var dbArticles = newsViewModel.newsRepository.getSavedArticle()
                here@ for (dArticle in dbArticles) {
                    for (aArticle in t.articles) {
                        Log.d("compare ", "${dArticle.url}  || ${aArticle.url} ")
                        if (dArticle.url == aArticle.url) {
                            aArticle.isFavourite = true
                            continue@here
                        }
                    }
                }
            }
            if (newsAdapter != null && t.articles.size > 0) {
                newsAdapter.differ.submitList(t.articles)
                binding.newsFragmentRecycler.setItemViewCacheSize(t.articles.size)
            }
        }

        when (fragmentName) {
            resources.getString(R.string.category) -> {
                binding.categoryText.visibility = View.VISIBLE
                binding.categoryText.text = category
                if (context?.let { isOnline(it) } == true) {
                    newsViewModel.getNews(null, 1, category)
                } else {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.no_internet_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            resources.getString(R.string.discover) -> {
                binding.categoryText.visibility = View.GONE
                if (context?.let { isOnline(it) } == true) {
                    if (pageNo + 1 == 3)
                        newsViewModel.searchNews("trending", null, "popularity", "20")
                    else
                        newsViewModel.getNews("us", pageNo + 1, null)
                } else {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.no_internet_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        newsViewModel.getSavedArticle()
        handleClicks(newsAdapter)
    }

    private fun handleClicks(newsAdapter: NewsAdapter) {
        newsAdapter.setOnShowMoreListener {
            var displayFragment = DisplayNewsFragment()
            var bundle = Bundle()
            bundle.putSerializable("article", it)
            displayFragment.arguments = bundle
            activity?.supportFragmentManager?.beginTransaction()
                ?.addToBackStack("")
                ?.replace(R.id.main_FrameL, displayFragment)?.commit()
        }

        newsAdapter.onSaveNewsClickListener {
            context?.let { it1 -> newsViewModel.insertArticle(it1, it) }
        }

        newsAdapter.onDeleteClickListener {
            it.url?.let { it1 -> newsViewModel.deleteArticle(it1) }
        }
        newsAdapter.onShareClickListener {
            shareNews((activity as MainActivity), it)
        }
    }
}