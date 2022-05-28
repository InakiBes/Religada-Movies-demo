package com.religada.moviesdemo.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.religada.moviesdemo.databinding.FragmentMovieDetailBinding
import com.religada.moviesdemo.ui.main.viewmodel.MainViewModel

class MovieDetailFragment : Fragment(){

    private lateinit var binding: FragmentMovieDetailBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)

        setDataOnScreen()

        return binding.root
    }

    private fun setDataOnScreen() {
        // TODO Hacer llamada a la api y mostrar los datos de la peli
    }


}