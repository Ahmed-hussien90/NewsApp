package com.newsapp90.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsapp90.R
import com.newsapp90.databinding.FragmentFavouriteBinding
import com.newsapp90.ui.MainActivity
import com.newsapp90.ui.NewsAdapter
import com.newsapp90.util.shareNews
import com.newsapp90.viewmodel.NewsViewModel


class FavouriteFragment : Fragment() {
    lateinit var newsViewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var binding: FragmentFavouriteBinding =
            FragmentFavouriteBinding.inflate(inflater, container, false)
        newsViewModel = (activity as MainActivity).newsViewModel
        var newsAdapter = NewsAdapter()
        newsViewModel.articleMutableLiveData.observe(viewLifecycleOwner) {
            if (newsAdapter != null) {
                newsAdapter.differ.submitList(it)
            }
        }
        handleClicks(newsAdapter)
        newsViewModel.getSavedArticle()
        binding.favRecycler.adapter = newsAdapter
        binding.favRecycler.layoutManager = LinearLayoutManager(context)

        return binding.root
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