package com.example.fastfooddiet.view

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.fastfooddiet.R
import com.example.fastfooddiet.data.MealData
import com.example.fastfooddiet.databinding.FragmentMealBinding
import com.example.fastfooddiet.databinding.GenericEmptyResultBinding
import com.example.fastfooddiet.view.child.MealFoodFragment
import com.example.fastfooddiet.view.child.MealNutritionFragment
import com.example.fastfooddiet.viewmodels.MealViewModel
import com.example.fastfooddiet.viewmodels.SharedViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MealFragment : Fragment() {

    private lateinit var mealViewModel: MealViewModel
    private val sharedViewModel: SharedViewModel by navGraphViewModels(R.id.nav_meal)
    private val args: MealFragmentArgs by navArgs()

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
        mealViewModel = ViewModelProvider(this).get(MealViewModel::class.java).apply {
            setMeal(args.mealId)
        }

        val binding = FragmentMealBinding
            .inflate(inflater, container, false).apply {
                viewmodel = mealViewModel
                lifecycleOwner = viewLifecycleOwner
                setupViewPager(mealFragPager, mealFragTabLayout, args.mealId)
                setupToolBar(activity as AppCompatActivity, mealFragToolbar)
                setupSharedViewModel(sharedViewModel, mealViewModel, args.mealId)
                setupEmptyResult(mealFragEmpty, mealViewModel)
            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Nutrition"
                else -> "Meal Items"
            }
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.meal_menu, menu)
        val deleteItem = menu.findItem(R.id.meal_menu_delete)

        //Show delete option only when viewPager is showing 'Meal Items'
        mealViewModel.isFoodView.observe(viewLifecycleOwner, Observer { isFoodView ->
            deleteItem.isVisible = isFoodView
        })

        //Change menu item title for multiple deletes
        sharedViewModel.isDeleteMode.observe(viewLifecycleOwner, Observer { isDeleteMode ->
            if (isDeleteMode)
                deleteItem.title = getString(R.string.menu_remove_items_on)
            else
                deleteItem.title = getString(R.string.menu_remove_items_off)
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.meal_menu_rename -> {
                goToUpdateNameDialog()
                true
            }
            R.id.meal_menu_delete -> {
                sharedViewModel.setDeleteMode(!sharedViewModel.isDeleteMode())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::viewPager.isInitialized)
            viewPager.unregisterOnPageChangeCallback(pageChangeCallback)
    }

    //**** METHODS ****

    private inner class PagerAdapter(fa: Fragment, val mealId: Int) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> MealNutritionFragment.newInstance(mealId)
                else -> MealFoodFragment.newInstance(mealId)
            }
        }
    }

    private inner class PageChangeCallBack() : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            when (position) {
                0 ->  {
                    mealViewModel.isFoodView.value = false
                    sharedViewModel.setDeleteMode(false)
                }
                else -> mealViewModel.isFoodView.value = true
            }
        }
    }

    private fun setupViewPager(_viewPager: ViewPager2, _tabLayout: TabLayout, mealId: Int) {
        pageChangeCallback = PageChangeCallBack()
        viewPager = _viewPager.apply {
            adapter = PagerAdapter(this@MealFragment, mealId)
            registerOnPageChangeCallback(pageChangeCallback)
        }
        tabLayout = _tabLayout
    }

    private fun setupToolBar(activity: AppCompatActivity, toolbar: Toolbar) {
        activity.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

        //Set back button to MainFragment
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupSharedViewModel(sharedModel: SharedViewModel,
                                     viewModel: MealViewModel,
                                     mealId: Int) {
        sharedModel.textInput.observe(viewLifecycleOwner, Observer { name ->
            if (sharedModel.textChanged && name.isNotBlank()) {
                sharedModel.textChanged = false

                //Update meal name
                viewModel.updateMeal(MealData(mealId, name))
                Toast.makeText(context, "Meal updated", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupEmptyResult(emptyLayout: GenericEmptyResultBinding,
                                 viewModel: MealViewModel) {
        emptyLayout.apply {
            emptyResultImage.setImageResource(R.drawable.ic_restaurant_menu)
            emptyResultHeader.setText(R.string.empty_meal_meal_header)
            emptyResultText.setText(R.string.empty_meal_meal_text)
        }

        viewModel.mealFoodCount.observe(viewLifecycleOwner, Observer { count ->
            if (count > 0) {
                emptyLayout.root.visibility = View.INVISIBLE
            } else {
                emptyLayout.root.visibility = View.VISIBLE
            }
        })
    }

    private fun goToUpdateNameDialog() {
        sharedViewModel.setDeleteMode(false)
        val action = MealFragmentDirections
            .toTextInputMealDialog(R.id.nav_meal, "Rename meal")
        findNavController().navigate(action)
    }
}
