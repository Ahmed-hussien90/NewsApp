package com.newsapp90.ui.fragments

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.newsapp90.databinding.FragmentDisplayNewsBinding
import com.newsapp90.model.Article
import com.newsapp90.ui.MainActivity
import com.newsapp90.viewmodel.NewsViewModel
import com.varunest.sparkbutton.SparkEventListener


class DisplayNewsFragment : Fragment() {

    lateinit var newsViewModel: NewsViewModel
    lateinit var binding: FragmentDisplayNewsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDisplayNewsBinding.inflate(inflater, container, false)
        displayNews(arguments)
        return binding.root
    }

    private fun displayNews(arguments: Bundle?) {
        if (arguments != null) {
            var news: Article = arguments.getSerializable("article") as Article
            var newstitle: String? = news.title
            var newsDate: String? = news.publishedAt
            if (newstitle?.contains('-') == true) {
                newstitle = newstitle.substring(0, newstitle.lastIndexOf('-'));
            }
            if (newsDate?.contains('T') == true) {
                newsDate = newsDate.substring(0, newsDate.indexOf('T'));
            }

            binding.displayedTitle.text = newstitle
            binding.displayedDescription.text = news.description
            binding.displayedDate.text = newsDate
            Glide.with(this).load(news.urlToImage).into(binding.displayedImage)
            binding.displayedUrl.movementMethod = LinkMovementMethod.getInstance()
            binding.displayedUrl.setText(news.url)
            onSaveButton()
        }

    }

    private fun onSaveButton() {

        newsViewModel = (activity as MainActivity).newsViewModel
        var news: Article = arguments?.getSerializable("article") as Article
        if (news.isFavourite) {
            binding.sparkButton.isChecked = true
        }

        binding.sparkButton.setEventListener(object : SparkEventListener {
            override fun onEvent(button: ImageView?, buttonState: Boolean) {
                if (buttonState) {
                    news.isFavourite = true
                    context?.let { newsViewModel.insertArticle(it, news) }
                } else {
                    // Button is inactive
                    news.url?.let { newsViewModel.deleteArticle(it) }
                }
            }

            override fun onEventAnimationEnd(button: ImageView?, buttonState: Boolean) {
            }

            override fun onEventAnimationStart(button: ImageView?, buttonState: Boolean) {
            }
        })
    }

}