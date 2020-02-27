package com.example.fastfooddiet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fastfooddiet.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    //**** LIFECYCLE METHODS ****

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

}