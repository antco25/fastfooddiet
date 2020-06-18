package com.example.fastfooddiet.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.fastfooddiet.R
import com.example.fastfooddiet.databinding.FragmentFavoriteBinding
import com.example.fastfooddiet.view.child.FavoriteChildFragment
import com.example.fastfooddiet.viewmodels.FavoriteViewModel
import com.example.fastfooddiet.viewmodels.SharedViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FavoriteFragment : Fragment() {

    private lateinit var favoriteViewModel: FavoriteViewModel
    private val sharedViewModel: SharedViewModel by navGraphViewModels(R.id.nav_favorites)

    private lateinit var viewPager: ViewPager2
    private lateinit var pageChangeCallback: ViewPager2.OnPageChangeCallback
    private lateinit var tabLayout: TabLayout

    //**** LIFECYCLE METHODS ****
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Get ViewModel
        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        val binding = FragmentFavoriteBinding
            .inflate(inflater, container, false).apply {
                fragment = this@FavoriteFragment
                viewmodel = favoriteViewModel
                sharedModel = sharedViewModel
                lifecycleOwner = viewLifecycleOwner
                pageChangeCallback = PageChangeCallBack()
                viewPager = favFragPager.apply {
                    adapter = PagerAdapter(this@FavoriteFragment)
                    registerOnPageChangeCallback(pageChangeCallback)
                }
                tabLayout = favFragTabLayout
                setupToolBar(activity as AppCompatActivity, favFragToolbar)
                setupSharedViewModel(favFragDelMealButton)
            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Food"
                else -> "Meals"
            }
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager.unregisterOnPageChangeCallback(pageChangeCallback)
    }

    //**** METHODS ****

    private inner class PagerAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment =
            FavoriteChildFragment.newInstance(position)

    }

    private inner class PageChangeCallBack()
        : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            when (position) {
                0 ->  {
                    favoriteViewModel.setIsFoods(true)
                    sharedViewModel.setDeleteMode(false)
                }
                else -> favoriteViewModel.setIsFoods(false)
            }

        }
    }

    private fun setupToolBar(activity: AppCompatActivity, toolbar: Toolbar) {
        activity.setSupportActionBar(toolbar)

        //Set back button to MainFragment
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupSharedViewModel(deleteMealIcon: ImageView) {
        sharedViewModel.textInput.observe(viewLifecycleOwner, Observer { name ->
            if (sharedViewModel.textChanged && name.isNotBlank()) {
                favoriteViewModel.addMeal(name)
                sharedViewModel.textChanged = false
            }
        })

        sharedViewModel.isDeleteMode.observe(viewLifecycleOwner, Observer { isDeleteMode ->
            if (isDeleteMode)
                deleteMealIcon.setImageResource(R.drawable.ic_delete_cancel)
            else
                deleteMealIcon.setImageResource(R.drawable.ic_delete)
        })
    }

    fun goToAddMealDialog() {
        sharedViewModel.setDeleteMode(false)
        val action = FavoriteFragmentDirections.toTextInputDialog(
            R.id.nav_favorites, "Create a New Meal")
        findNavController().navigate(action)
    }
}
