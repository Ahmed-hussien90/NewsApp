package com.newsapp90.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsapp90.R
import com.newsapp90.databinding.FragmentSearchBinding
import com.newsapp90.ui.MainActivity
import com.newsapp90.ui.NewsAdapter
import com.newsapp90.util.Constants
import com.newsapp90.util.isOnline
import com.newsapp90.util.shareNews
import com.newsapp90.viewmodel.NewsViewModel


class SearchFragment : Fragment() {


    lateinit var newsViewModel: NewsViewModel
    lateinit var binding: FragmentSearchBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.searchView.addTextChangedListener {
            if (binding.searchView.text?.isEmpty() == false)
                search()
        }

        binding.languageAutocomplete.setOnItemClickListener { _, _, _, _ ->
            if (binding.searchView.text?.isEmpty() == false)
                search()
        }

        binding.sortedByAutocomplete.setOnItemClickListener { _, _, _, _ ->
            if (binding.searchView.text?.isEmpty() == false)
                search()
        }

        binding.maxResultsAutocomplete.setOnItemClickListener { _, _, _, _ ->
            if (binding.searchView.text?.isEmpty() == false)
                search()
        }
        return binding.root
    }

    private fun prepareAdapters() {
        val categoryAdapter =
            ArrayAdapter(requireContext(), R.layout.list_item, Constants.results)
        binding.maxResultsAutocomplete.setAdapter(categoryAdapter)
        val languageAdapter =
            ArrayAdapter(requireContext(), R.layout.list_item, Constants.languageList)
        binding.languageAutocomplete.setAdapter(languageAdapter)
        val sortAdapter = ArrayAdapter(requireContext(), R.layout.list_item, Constants.sortedByList)
        binding.sortedByAutocomplete.setAdapter(sortAdapter)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun search() {

        binding.shimmerLayoutSearch.visibility = View.VISIBLE
        binding.shimmerLayoutSearch.startShimmer()
        var searchNewsAdapter = NewsAdapter()

        binding.searchNewsRecycler.adapter = searchNewsAdapter

        binding.searchNewsRecycler.layoutManager =
            LinearLayoutManager(context)

        newsViewModel = (activity as MainActivity).newsViewModel

        newsViewModel.mutableLiveData.observe(viewLifecycleOwner) { t ->

            binding.shimmerLayoutSearch.stopShimmer()
            binding.shimmerLayoutSearch.visibility = View.GONE
            binding.searchNewsRecycler.visibility = View.VISIBLE
            if (searchNewsAdapter != null) {
                searchNewsAdapter.differ.submitList(t.articles)
            }
        }


        if (context?.let { isOnline(it) } == true) {
            newsViewModel.searchNews(
                binding.searchView.text.toString(),
                binding.languageAutocomplete.text.toString(),
                binding.sortedByAutocomplete.text.toString(),
                binding.maxResultsAutocomplete.text.toString()
            )
        } else {
            Toast.makeText(
                context,
                resources.getString(R.string.no_internet_connection),
                Toast.LENGTH_SHORT
            ).show()
        }

        handleClicks(searchNewsAdapter)
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

    override fun onResume() {
        super.onResume()
        prepareAdapters()
        if (binding.searchView.text?.isEmpty() == false)
            search()
    }
}