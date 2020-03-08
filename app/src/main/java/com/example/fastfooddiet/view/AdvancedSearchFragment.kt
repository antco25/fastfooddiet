package com.example.fastfooddiet.view


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fastfooddiet.data.SearchParams
import com.example.fastfooddiet.databinding.FragmentAdvancedSearchBinding

//TODO: Advanced Search
class AdvancedSearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAdvancedSearchBinding.inflate(inflater,container,false)

        binding.button.setOnClickListener {
            val action = AdvancedSearchFragmentDirections
                .actionAdvancedSearchFragmentToFoodListFragment("Custom Search",
                    false, true,
                    true, getSearchParams(binding))
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun getSearchParams(binding : FragmentAdvancedSearchBinding) : SearchParams {
        val restaurants : List<String>? = listOf("KFC", "Burger King")
        val foodType : List<String>? = null
        val minCalories : Int = getValue(binding.filterMinCalories.text.toString())
        val maxCalories : Int = getValue(binding.filterMaxCalories.text.toString())
        return SearchParams("", restaurants, foodType, minCalories, maxCalories)
    }

    private fun getValue(text : String) : Int {
        if (text.isBlank())
            return -1
        else
            return text.toInt()
    }


}
