package com.example.giphydemo

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.giphydemo.databinding.ActivityMainBinding
import com.example.giphydemo.ui.main.adapter.SectionsPagerAdapter
import com.example.giphydemo.ui.main.common.BaseActivity
import com.google.android.material.tabs.TabLayout

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
    }
}