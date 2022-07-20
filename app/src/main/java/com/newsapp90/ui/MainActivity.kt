package com.newsapp90.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.newsapp90.R
import com.newsapp90.model.NewsData
import com.newsapp90.ui.fragments.CategoriesFragment
import com.newsapp90.ui.fragments.DiscoverFragment
import com.newsapp90.ui.fragments.FavouriteFragment
import com.newsapp90.ui.fragments.SearchFragment
import com.newsapp90.viewmodel.NewsViewModel

class MainActivity : AppCompatActivity() {


    lateinit var bNavigation: BottomNavigationView
    lateinit var discoverFragment: DiscoverFragment
    lateinit var favouriteFragment: FavouriteFragment
    lateinit var searchFragment: SearchFragment
    lateinit var categoriesFragment: CategoriesFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        bNavigation = findViewById(R.id.bottom_navigation)
        categoriesFragment = CategoriesFragment()
        searchFragment = SearchFragment()
        discoverFragment = DiscoverFragment()
        favouriteFragment = FavouriteFragment()

        supportFragmentManager.beginTransaction().replace(R.id.main_FrameL, discoverFragment)
            .commit()

        bNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.search_page -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_FrameL, searchFragment).commit()
                    return@setOnItemSelectedListener true

                }
                R.id.category_page -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_FrameL, categoriesFragment).commit()
                    return@setOnItemSelectedListener true

                }
                R.id.discover_page -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_FrameL, discoverFragment).commit()
                    return@setOnItemSelectedListener true

                }
                R.id.fav_page -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_FrameL, favouriteFragment).commit()
                    return@setOnItemSelectedListener true

                }

            }
            return@setOnItemSelectedListener false
        }


    }
}