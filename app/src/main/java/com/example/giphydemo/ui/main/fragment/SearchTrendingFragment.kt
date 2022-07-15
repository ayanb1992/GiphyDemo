package com.example.giphydemo.ui.main.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.giphydemo.databinding.FragmentSearchTrendingBinding
import com.example.giphydemo.ui.main.adapter.TrendingAdapter
import com.example.giphydemo.ui.main.common.BaseFragment
import com.example.giphydemo.util.hideSoftKeyboard
import com.example.giphydemo.viewmodel.SearchTrendingViewModel


class SearchTrendingFragment : BaseFragment() {

    private lateinit var searchTrendingViewModel: SearchTrendingViewModel
    private var binding: FragmentSearchTrendingBinding? = null
    private var adapter: TrendingAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchTrendingViewModel = ViewModelProvider(this)[SearchTrendingViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchTrendingBinding.inflate(inflater, container, false)
        val root = binding?.root
        setupObservers()
        setupListeners()
        return (root as View)
    }

    private fun setupObservers() {
        searchTrendingViewModel.gifsResponse.observe(viewLifecycleOwner) { gifResponse ->
            val gifSearchResultView: RecyclerView = binding?.gifSearchResultView!!
            val layoutManager = LinearLayoutManager(requireContext())
            gifSearchResultView.layoutManager = layoutManager

            if (adapter == null) {
                adapter = TrendingAdapter(requireContext(), gifResponse.data)
                gifSearchResultView.adapter = adapter
            } else {
                adapter?.setGifData(gifResponse.data)
            }
        }
    }

    private fun setupListeners() {
        binding?.gifSearchImgButton?.setOnClickListener {
            binding?.gifSearchView?.hideSoftKeyboard(requireContext())
            val queryString = binding?.gifSearchView?.query?.toString()
            if(queryString?.isNotEmpty() == true) {
                searchTrendingViewModel.searchGifs(queryString)
            }
        }

        binding?.gifSearchView?.setOnCloseListener {
            binding?.gifSearchView?.hideSoftKeyboard(requireContext())
            searchTrendingViewModel.getTrendingGifs()
            false
        }
    }

    override fun onStart() {
        super.onStart()
        searchTrendingViewModel.getTrendingGifs()
    }

    companion object {
        @JvmStatic
        fun newInstance(): SearchTrendingFragment {
            return SearchTrendingFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}