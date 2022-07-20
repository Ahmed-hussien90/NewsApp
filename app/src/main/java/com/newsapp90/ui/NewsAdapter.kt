package com.newsapp90.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.newsapp90.R
import com.newsapp90.model.Article
import kotlin.math.min

class NewsAdapter(context: Context, layout: Int) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private val mData = mutableListOf<Article>()
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    val context = context
    val layout: Int = layout

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val news: Article = mData[position]
        var newstitle: String? = news.title
        holder.newsTitle.setText(newstitle?.substring(0, newstitle.lastIndexOf('-')))
        holder.newsDate?.setText(news.publishedAt)
        Glide.with(context).load(news.urlToImage).into(holder.newsImage)
    }

    override fun getItemCount(): Int {
        if (layout == R.layout.news_list_item) {
            return min(mData.size, 4)
        }
        return mData.size
    }

    fun setlist(data: MutableList<Article>) {
        mData.clear()
        mData.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var newsTitle: TextView
        var newsDate: TextView? = null
        var newsImage: ShapeableImageView

        init {
            if (layout == R.layout.news_list_item) {
                newsTitle = itemView.findViewById(R.id.news_title)
                newsDate = itemView.findViewById(R.id.news_date)
                newsImage = itemView.findViewById(R.id.news_Image)
            } else {
                newsTitle = itemView.findViewById(R.id.news_title1)
                newsImage = itemView.findViewById(R.id.news_Image1)
            }
        }
    }

}