package com.example.fastfooddiet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.fastfooddiet.databinding.FragmentDetailBinding
import com.example.fastfooddiet.viewmodels.DetailViewModel

class DetailFragment : Fragment() {

    //**** PROPERTIES ****
    private lateinit var detailViewModel: DetailViewModel
    private val args : DetailFragmentArgs by navArgs()

    //**** LIFECYCLE METHODS ****
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Get ViewModel
        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
            .apply {setFood(args.foodId)}

        val binding = FragmentDetailBinding.inflate(inflater, container, false)
            .apply {
                viewmodel = detailViewModel
                lifecycleOwner = viewLifecycleOwner
            }

        return binding.root
    }

}