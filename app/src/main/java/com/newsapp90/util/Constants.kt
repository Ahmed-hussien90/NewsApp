package com.newsapp90.util

class Constants {

    companion object {
        const val BASE_URL: String = "https://newsapi.org/"
        const val API_KEY: String = "718cd85bb6204edda75a6607a64f7574"
        val categoriesList =
            listOf("general", "business", "health", "science", "sports", "technology")
        val results = listOf("10", "20", "50", "100")
        val sortedByList = listOf("popularity", "relevancy", "publishedAt")
        val languageList =
            listOf("All", "ar", "de", "en", "es", "fr", "it", "pt", "ru", "sv", "ud", "zh")

    }

}