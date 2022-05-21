package com.religada.moviesdemo.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.activityViewModels
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.religada.moviesdemo.databinding.FragmentFavoriteMoviesBinding
import com.religada.moviesdemo.databinding.FragmentMainBinding
import com.religada.moviesdemo.navigator.AppNavigator
import com.religada.moviesdemo.ui.main.viewmodel.MainViewModel
import javax.inject.Inject

class FavoritesMoviesFragment @Inject constructor(
    val navigator: AppNavigator,
) : Fragment() {

    private lateinit var binding: FragmentFavoriteMoviesBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteMoviesBinding.inflate(inflater, container, false)

        return binding.root
    }

}