package com.religada.moviesdemo.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.religada.moviesdemo.R
import com.religada.moviesdemo.data.repository.MovieRepositoryLocal
import com.religada.moviesdemo.databinding.FragmentMainBinding
import com.religada.moviesdemo.navigator.AppNavigator
import com.religada.moviesdemo.ui.main.adapter.ViewPagerMoviesAdapter
import com.religada.moviesdemo.ui.main.viewmodel.MainViewModel
import javax.inject.Inject

class MainFragment @Inject constructor(
    private val navigator: AppNavigator,
    private val repositoryLocal: MovieRepositoryLocal
) : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    private val titlesArray = arrayOf(R.string.popular, R.string.favorites)
    private val iconsArray = arrayOf(R.drawable.ic_home, R.drawable.ic_favorite)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        setUpViewPager()

        return binding.root
    }

    private fun setUpViewPager() {
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        activity?.let {
            val adapter = ViewPagerMoviesAdapter(childFragmentManager, lifecycle, navigator, repositoryLocal)
            viewPager.adapter = adapter

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.setText(titlesArray[position])
                tab.setIcon(iconsArray[position])
            }.attach()
        }
    }
}