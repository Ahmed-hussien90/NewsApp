package com.newsapp90.util

class Constants {

    companion object {
        const val BASE_URL: String = "https://newsapi.org/"
        const val API_KEY: String = "cf0e2f53c2654e9794f5c99912653810"
        val categoriesList =
            listOf("general", "business", "health", "science", "sports", "technology")
        val results = listOf("10", "20", "50", "100")
        val sortedByList = listOf("popularity", "relevancy", "publishedAt")
        val languageList =
            listOf("All", "ar", "de", "en", "es", "fr", "it", "pt", "ru", "sv", "ud", "zh")

    }

}