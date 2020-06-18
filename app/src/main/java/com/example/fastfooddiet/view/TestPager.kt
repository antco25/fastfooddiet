package com.example.fastfooddiet.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.fastfooddiet.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TestPager : Fragment() {

    private lateinit var viewPager: ViewPager2

    //**** LIFECYCLE METHODS ****
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.test_pager, container, false)
            .apply {
                // Instantiate a ViewPager2 and a PagerAdapter.
                viewPager = findViewById(R.id.test_pager)

                // The pager adapter, which provides the pages to the view pager widget.
                val pagerAdapter = PagerAdapter(this@TestPager)
                viewPager.adapter = pagerAdapter
            }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val tabLayout : TabLayout = view.findViewById(R.id.test_pager_tabLayout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "Fragment $position"
        }.attach()
    }

    //**** METHODS ****

    private inner class PagerAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment = TestChild().apply {
            setDefaultText("This is Fragment $position")
        }
    }

}
