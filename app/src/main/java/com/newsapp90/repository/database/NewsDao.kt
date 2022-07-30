package com.newsapp90.repository.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.newsapp90.model.Article

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getArticles(): MutableList<Article>

    @Query("DELETE FROM articles WHERE url = :articleUrl")
    fun deleteArticle(articleUrl: String)
}