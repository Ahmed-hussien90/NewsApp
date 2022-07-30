package com.newsapp90.ui

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.newsapp90.R
import com.newsapp90.databinding.ActivityMainBinding
import com.newsapp90.repository.NewsRepository
import com.newsapp90.repository.database.NewsDataBase
import com.newsapp90.ui.fragments.*
import com.newsapp90.util.isOnline
import com.newsapp90.viewmodel.NewsViewModel
import com.newsapp90.viewmodel.NewsViewModelFactory
import java.io.Serializable
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity(), Serializable, OnTabSelectedListener {

    lateinit var binding: ActivityMainBinding
    lateinit var newsViewModel: NewsViewModel
    var tapNo by Delegates.notNull<Int>()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val newsRepository = NewsRepository(NewsDataBase.getDatabase(this))
        val viewModelProvider = NewsViewModelFactory(newsRepository)
        newsViewModel = ViewModelProvider(this, viewModelProvider)[NewsViewModel::class.java]

        tapNo = binding.taplayout.selectedTabPosition
        loadNews()
        binding.taplayout.addOnTabSelectedListener(this)
        binding.bottomNavigation.setOnItemSelectedListener {
            return@setOnItemSelectedListener startFragment(it)
        }
        binding.menuButton.setOnClickListener { binding.drawer.openDrawer(GravityCompat.START) }

        binding.swipeLayout.setOnRefreshListener {
            loadNews()
            binding.swipeLayout.isRefreshing = false
        }
    }


    private fun startFragment(it: MenuItem): Boolean {
        when (it.itemId) {
            R.id.search_page -> {
                binding.pageName.setText(R.string.SearchNews)
                binding.taplayout.visibility = View.GONE
                binding.swipeLayout.isEnabled = false
                return fragmentTransaction(SearchFragment())
            }
            R.id.category_page -> {
                binding.pageName.setText(R.string.category)
                binding.taplayout.visibility = View.GONE
                binding.swipeLayout.isEnabled = false
                return fragmentTransaction(CategoriesFragment())
            }
            R.id.discover_page -> {
                binding.pageName.setText(R.string.home)
                binding.taplayout.visibility = View.VISIBLE
                binding.swipeLayout.isEnabled = true
                return loadNews()
            }
            R.id.fav_page -> {
                binding.pageName.setText(R.string.fav)
                binding.taplayout.visibility = View.GONE
                binding.swipeLayout.isEnabled = false
                return fragmentTransaction(FavouriteFragment())
            }
        }
        return false
    }


    private fun loadNews(): Boolean {
        var newsFragment = NewsFragment()
        var bundle = Bundle()
        bundle.putString("fragmentName", resources.getString(R.string.discover))
        bundle.putInt("sortedBy", tapNo)
        newsFragment.arguments = bundle
        return fragmentTransaction(newsFragment)
    }

    private fun fragmentTransaction(fragment: Fragment): Boolean {

        supportFragmentManager.beginTransaction().setCustomAnimations(
            android.R.anim.slide_in_left,
            android.R.anim.slide_out_right
        ).addToBackStack("ss")
            .replace(R.id.main_FrameL, fragment).commit()

        return true
    }

    override fun onBackPressed() {
        val f = supportFragmentManager.findFragmentById(R.id.main_FrameL)
        if (f is SearchFragment || f is CategoriesFragment || f is FavouriteFragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_FrameL, NewsFragment()).commit()
            binding.bottomNavigation.menu.findItem(R.id.discover_page).isChecked = true
        } else if (f is NewsFragment && f.arguments?.get("fragmentName") == resources.getString(R.string.category)) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_FrameL, CategoriesFragment()).commit()
        } else if (f is NewsFragment && f.arguments?.get("fragmentName") == resources.getString(R.string.discover)) {
            finishAffinity()
        } else {
            super.onBackPressed()
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        binding.mainFrameL.visibility = View.GONE
        if (tab != null) {
            tapNo = tab.position
        }
        loadNews()
        binding.mainFrameL.visibility = View.VISIBLE
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    override fun onResume() {
        super.onResume()
        loadNews()
    }

}