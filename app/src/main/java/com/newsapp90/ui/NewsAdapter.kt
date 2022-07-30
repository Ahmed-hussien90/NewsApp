package com.newsapp90.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.newsapp90.R
import com.newsapp90.databinding.NewsListItem2Binding
import com.newsapp90.model.Article
import com.varunest.sparkbutton.SparkEventListener

class NewsAdapter :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ViewHolder(var view: NewsListItem2Binding) : RecyclerView.ViewHolder(view.root)

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<NewsListItem2Binding>(
            inflater,
            R.layout.news_list_item2,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val news: Article = differ.currentList[position]
        holder.view.article = news
        holder.view.ReadMoreButton.setOnClickListener {
            onShowMoreClick?.let {
                it(news)
            }
        }
        holder.view.shareButton.setOnClickListener {
            onShareClick?.let {
                it(news)
            }
        }

        holder.view.sparkButton1.setEventListener(object : SparkEventListener {
            override fun onEvent(button: ImageView?, buttonState: Boolean) {
                if (buttonState) {
                    news.isFavourite = true
                    onSaveNewsClick?.let {
                        Log.d("saved", news.toString())
                        it(news)
                    }
                } else {
                    // Button is inactive
                    onArticleDeleteClick?.let {
                        it(news)
                    }
                }
            }

            override fun onEventAnimationEnd(button: ImageView?, buttonState: Boolean) {
            }

            override fun onEventAnimationStart(button: ImageView?, buttonState: Boolean) {
            }
        })
        if (news.isFavourite) {
            holder.view.sparkButton1.isChecked = true
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemId(position: Int) = position.toLong()

    private var onShowMoreClick: ((Article) -> Unit)? = null
    private var onSaveNewsClick: ((Article) -> Unit)? = null
    private var onArticleDeleteClick: ((Article) -> Unit)? = null
    private var onShareClick: ((Article) -> Unit)? = null


    fun setOnShowMoreListener(listener: ((Article) -> Unit)) {
        onShowMoreClick = listener
    }

    fun onSaveNewsClickListener(listener: (Article) -> Unit) {
        onSaveNewsClick = listener
    }

    fun onDeleteClickListener(listener: (Article) -> Unit) {
        onArticleDeleteClick = listener
    }

    fun onShareClickListener(listener: (Article) -> Unit) {
        onShareClick = listener
    }

}