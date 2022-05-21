package com.religada.moviesdemo.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.religada.moviesdemo.R
import com.religada.moviesdemo.data.model.MovieResponse
import com.religada.moviesdemo.data.repository.MovieRepositoryLocal
import com.religada.moviesdemo.databinding.FragmentMoviesListBinding
import com.religada.moviesdemo.navigator.AppNavigator
import com.religada.moviesdemo.ui.main.adapter.RecyclerViewAdapterMovies
import com.religada.moviesdemo.ui.main.viewmodel.MainViewModel
import com.religada.moviesdemo.widget.PaginationScrollListener
import javax.inject.Inject

class PopularMoviesFragment @Inject constructor(
    private val navigator: AppNavigator,
    private val repositoryLocal: MovieRepositoryLocal
) : Fragment() {

    private lateinit var binding: FragmentMoviesListBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var adapterRecycler: RecyclerViewAdapterMovies
    private var itemsList = listOf<MovieResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesListBinding.inflate(inflater, container, false)

        setSearchView()
        setCloseSearchButton()
        setRecyclerViewList()

        return binding.root
    }

    private fun setRecyclerViewList() {
        showProgressBar()
        adapterRecycler = RecyclerViewAdapterMovies(
            mutableListOf(),
            navigator,
            repositoryLocal,
        ) { movieId, isFavorite ->
            makeFavorite(movieId, isFavorite)
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
        mainViewModel.getPopularMovies() {
            if (it.isNotEmpty()) {
                binding.recyclerViewList.isVisible = true
                updateRecyclerViewForSearching(it)
                if (itemsList.isEmpty())
                    itemsList = it
            }
            hideProgressBar(it.isNotEmpty())
        }
    }

    private fun setAutoLoadMoreSessions(layoutManager: LinearLayoutManager) {
        binding.recyclerViewList.addOnScrollListener(object :
            PaginationScrollListener(layoutManager) {
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

    private fun makeFavorite(movie: MovieResponse, isFavorite: Boolean) {
        mainViewModel.makeFavorite(movie, isFavorite)
    }

    private fun setSearchView() {
        activity?.findViewById<SearchView>(R.id.searcher)?.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    setDataOnRecyclerViewServicesBySearching(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    setDataOnRecyclerViewServicesBySearching(newText)

                    return true
                }
            }
        )
    }

    private fun setCloseSearchButton() {
        activity?.let {
            val searchView = it.findViewById<SearchView>(R.id.searcher)
            val closeBtn: View = searchView.findViewById(androidx.appcompat.R.id.search_close_btn)
            closeBtn.setOnClickListener {
                searchView.setQuery("", false) // reset Query text to be empty without submition
                searchView.isIconified = true // Replace the x icon with the search icon
                setInitialDataOnRecyclerView()
            }
        }
    }

    private fun setDataOnRecyclerViewServicesBySearching(key: String?) {
        if (key.isNullOrEmpty())
            return
        showProgressBar()
        mainViewModel.getMoviesByKeyword(key) {
            updateRvForSearching(it)
            if (itemsList.isEmpty())
                itemsList = it
            hideProgressBar(it.isNotEmpty())
        }
    }

    private fun updateRvForSearching(itemList: List<MovieResponse>) {
        activity?.runOnUiThread {
            adapterRecycler.setUpdatedData(itemList)
        }
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