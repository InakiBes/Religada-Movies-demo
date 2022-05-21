package com.religada.moviesdemo.ui.main.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.religada.moviesdemo.R
import com.religada.moviesdemo.data.local.FavoriteMovieRoom
import com.religada.moviesdemo.databinding.FragmentMoviesListBinding
import com.religada.moviesdemo.navigator.AppNavigator
import com.religada.moviesdemo.ui.main.adapter.RecyclerViewAdapterFavorites
import com.religada.moviesdemo.ui.main.viewmodel.MainViewModel
import javax.inject.Inject

class FavoritesMoviesFragment @Inject constructor(
    private val navigator: AppNavigator,
) : Fragment() {

    private lateinit var binding: FragmentMoviesListBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var adapterRecycler : RecyclerViewAdapterFavorites
    private var itemsList = listOf<FavoriteMovieRoom>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesListBinding.inflate(inflater, container, false)

        setRecyclerViewFavorites()

        return binding.root
    }

    override fun onResume() {
        activity?.runOnUiThread{
            setUpdatedData()
        }
        super.onResume()
    }

    private fun setRecyclerViewFavorites() {
        adapterRecycler = RecyclerViewAdapterFavorites(
            mutableListOf(),
            navigator
        ){ movieId ->
            showDialogConfirmation(){
                mainViewModel.deleteFavoriteById(movieId)
            }
        }
        binding.recyclerViewList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = adapterRecycler
        }
        setInitialDataOnRecyclerView()
    }

    private fun setInitialDataOnRecyclerView() {
        mainViewModel.getAllFavorites(){
            if(it.isNotEmpty()) {
                binding.recyclerViewList.isVisible = true
                updateRecyclerViewForSearching(it)
                if (itemsList.isEmpty())
                    itemsList = it
                hideProgressBar(true)
            }
        }
    }

    private fun updateRecyclerViewForSearching(list: List<FavoriteMovieRoom>) {
        activity?.runOnUiThread {
            adapterRecycler.setUpdatedData(list)
        }
    }

    fun setUpdatedData() {
        mainViewModel.getAllFavoritesLiveData().observe(viewLifecycleOwner, Observer { favorites ->
            adapterRecycler.setUpdatedData(favorites)
            binding.tvNoData.isVisible = favorites.isEmpty()
        })
    }

    private fun showDialogConfirmation(onResponse: () -> Unit) {
        val dialog = Dialog(requireContext())
        with(dialog) {
            requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(true)
            setContentView(R.layout.dialog_yes_no)
        }
        // Texts
        dialog.findViewById<TextView>(R.id.tvTitle).setText(R.string.remove_favorite)
        dialog.findViewById<TextView>(R.id.tv_info).setText(R.string.are_you_sure_remove_favorite)
        // Buttons
        dialog.findViewById<Button>(R.id.bt_yes).setOnClickListener {
            dialog.dismiss()
            onResponse()
        }
        dialog.findViewById<Button>(R.id.bt_no).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
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