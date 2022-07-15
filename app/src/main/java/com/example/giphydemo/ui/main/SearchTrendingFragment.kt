package com.example.giphydemo.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.giphydemo.databinding.FragmentSearchTrendingBinding
import com.example.giphydemo.ui.main.common.BaseFragment

class SearchTrendingFragment : BaseFragment() {

    private lateinit var searchTrendingViewModel: SearchTrendingViewModel
    private var _binding: FragmentSearchTrendingBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchTrendingViewModel = ViewModelProvider(this)[SearchTrendingViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchTrendingBinding.inflate(inflater, container, false)
        val root = binding.root

        val textView: TextView = binding.sectionLabel
        searchTrendingViewModel.trendingGifsResponse.observe(viewLifecycleOwner) { gifResponse ->
            val sb = StringBuffer()
            gifResponse?.data?.map { sb.append(it.title+"\n") }
            textView.text = sb.toString()
        }
        return root
    }

    override fun onStart() {
        super.onStart()
        searchTrendingViewModel.getTrendingGifs()
    }

    companion object {
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(): SearchTrendingFragment {
            return SearchTrendingFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}