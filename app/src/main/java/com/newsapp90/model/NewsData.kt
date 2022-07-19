package com.newsapp90.model

import com.google.gson.annotations.SerializedName

data class NewsData(
    @SerializedName("articles")
    var articles: MutableList<Article>,
    @SerializedName("status")
    var status: String?,
    @SerializedName("totalResults")
    var totalResults: Int?
)

