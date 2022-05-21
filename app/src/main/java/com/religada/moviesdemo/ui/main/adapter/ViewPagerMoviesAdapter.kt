package com.religada.moviesdemo.ui.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.religada.moviesdemo.navigator.AppNavigator
import com.religada.moviesdemo.ui.main.fragment.FavoritesMoviesFragment
import com.religada.moviesdemo.ui.main.fragment.PopularMoviesFragment

class ViewPagerMoviesAdapter (
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    val navigator: AppNavigator,
): FragmentStateAdapter(fragmentManager, lifecycle) {

    private val NUM_TABS = 2

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PopularMoviesFragment(navigator)
            else -> FavoritesMoviesFragment(navigator)
        }
    }
}


