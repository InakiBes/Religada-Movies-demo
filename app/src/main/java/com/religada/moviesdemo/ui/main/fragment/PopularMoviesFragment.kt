package com.religada.moviesdemo.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.religada.moviesdemo.data.model.MovieResponse
import com.religada.moviesdemo.databinding.FragmentPopularMoviesBinding
import com.religada.moviesdemo.navigator.AppNavigator
import com.religada.moviesdemo.ui.main.adapter.RecyclerViewAdapterMovies
import com.religada.moviesdemo.ui.main.viewmodel.MainViewModel
import com.religada.moviesdemo.widget.PaginationScrollListener
import javax.inject.Inject

class PopularMoviesFragment @Inject constructor(
    val navigator: AppNavigator,
) : Fragment() {

    private lateinit var binding: FragmentPopularMoviesBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var adapterRecycler : RecyclerViewAdapterMovies
    private var itemsList = listOf<MovieResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPopularMoviesBinding.inflate(inflater, container, false)

        setRecyclerViewList()
        
        return binding.root
    }

    private fun setRecyclerViewList() {
        showProgressBar()
        adapterRecycler = RecyclerViewAdapterMovies(
            mutableListOf(),
            navigator,
        ){ movieId ->
            makeFavorite(movieId)
        }
        val layoutManagerSessions = LinearLayoutManager(activity)
        binding.recyclerViewList.apply {
            layoutManager = layoutManagerSessions
            adapter = adapterRecycler
        }
        setInitialDataOnRecyclerView()
        setAutoLoadMoreSessions(layoutManagerSessions)
    }

    private fun setInitialDataOnRecyclerView() {
        mainViewModel.getPopularMovies(){
            if(it.isNotEmpty()) {
                binding.recyclerViewList.isVisible = true
                updateRecyclerViewForSearching(it)
                if (itemsList.isEmpty())
                    itemsList = it
            }
            hideProgressBar(it.isNotEmpty())
        }
    }

    private fun setAutoLoadMoreSessions(layoutManager: LinearLayoutManager) {
        binding.recyclerViewList.addOnScrollListener(object : PaginationScrollListener( layoutManager ) {
            override fun loadMoreItems() {
                mainViewModel.isLoading = true
                showProgressBar()
                mainViewModel.page++

                mainViewModel.getPopularMovies() {
                    if (it.isNotEmpty()) {
                        itemsList = itemsList + it
                        updateRecyclerViewForSearching(itemsList)
                    }
                    mainViewModel.isLoading = false
                    hideProgressBar(itemsList.isNotEmpty())
                }
            }

            override val isLastPage: Boolean
                get() = mainViewModel.isLastPage
            override val isLoading: Boolean
                get() = mainViewModel.isLoading
        })
    }

    private fun updateRecyclerViewForSearching(list: List<MovieResponse>) {
        activity?.runOnUiThread {
            adapterRecycler.setUpdatedData(list)
        }
    }

    private fun makeFavorite(movieId: Int){

    }

    private fun hideProgressBar(hasMovies: Boolean) {
        activity?.runOnUiThread {
            binding.progressList.visibility = View.GONE
            binding.tvNoData.isVisible = !hasMovies
        }
    }

    private fun showProgressBar() {
        activity?.runOnUiThread {
            binding.progressList.visibility = View.VISIBLE
        }
    }
}