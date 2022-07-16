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
import com.example.giphydemo.model.GifData
import com.example.giphydemo.ui.main.adapter.TrendingAdapter
import com.example.giphydemo.ui.main.common.BaseFragment
import com.example.giphydemo.util.hideSoftKeyboard
import com.example.giphydemo.viewmodel.SearchTrendingViewModel


class SearchTrendingFragment : BaseFragment(), TrendingAdapter.OnFavoriteClickListener {

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

    private fun showLoader() {
        binding?.loaderLayout?.root?.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        binding?.loaderLayout?.root?.visibility = View.GONE
    }

    private fun setupObservers() {
        searchTrendingViewModel.gifsResponse.observe(viewLifecycleOwner) { gifResponse ->
            hideLoader()
            if(gifResponse.data.isNotEmpty()) {
                binding?.noFavFoundTv?.visibility = View.GONE
                val gifSearchResultView: RecyclerView = binding?.gifSearchResultView!!
                val layoutManager = LinearLayoutManager(requireContext())
                gifSearchResultView.layoutManager = layoutManager

                if (adapter == null) {
                    adapter = TrendingAdapter(requireContext(), gifResponse.data)
                    (adapter as TrendingAdapter).setOnFavoriteClickListener(this@SearchTrendingFragment)
                    gifSearchResultView.adapter = adapter
                } else {
                    adapter?.setGifData(gifResponse.data)
                }
            } else {
                binding?.noFavFoundTv?.visibility = View.VISIBLE
                adapter?.clearGifData()
            }
        }

        searchTrendingViewModel.insertComplete.observe(viewLifecycleOwner) {
            if (it != null && it.first) {
                val id = it.second
                var indexChanged = -1
                adapter?.getGifData()?.forEachIndexed { index, gifData ->
                    if (gifData.id == id) {
                        gifData.isFavorite = true
                        indexChanged = index
                    }
                }
                if (indexChanged != -1) adapter?.notifyItemChanged(indexChanged)
            }
        }

        searchTrendingViewModel.removeComplete.observe(viewLifecycleOwner) {
            if (it != null && it.first) {
                val id = it.second
                var indexChanged = -1
                adapter?.getGifData()?.forEachIndexed { index, gifData ->
                    if (gifData.id == id) {
                        gifData.isFavorite = false
                        indexChanged = index
                    }
                }
                if (indexChanged != -1) adapter?.notifyItemChanged(indexChanged)
            }
        }
    }

    private fun setupListeners() {
        binding?.gifSearchImgButton?.setOnClickListener {
            binding?.gifSearchEditText?.hideSoftKeyboard(requireContext())
            val queryString = binding?.gifSearchEditText?.text?.toString()
            if (queryString?.isNotEmpty() == true) {
                searchGifs(queryString)
            }
        }

        binding?.gifSearchView?.setEndIconOnClickListener {
            binding?.apply {
                gifSearchEditText.clearFocus()
                gifSearchEditText.hideSoftKeyboard(requireContext())
                if (gifSearchEditText.text?.isNotEmpty() == true) {
                    gifSearchEditText.setText("")
                    loadTrendingGifsList()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        loadTrendingGifsList()
    }

    override fun onResume() {
        super.onResume()
        if (adapter != null && binding?.gifSearchEditText?.text?.toString()?.isEmpty() == true) {
            adapter?.clearGifData()
            loadTrendingGifsList()
        } else if (adapter != null && binding?.gifSearchEditText?.text?.toString()?.isNotEmpty() == true) {
            searchGifs(binding?.gifSearchEditText?.text?.toString() ?: "")
        }
    }

    private fun searchGifs(queryString: String) {
        showLoader()
        adapter?.clearGifData()
        binding?.noFavFoundTv?.visibility = View.GONE
        searchTrendingViewModel.searchGifs(queryString)
    }

    private fun loadTrendingGifsList() {
        showLoader()
        binding?.noFavFoundTv?.visibility = View.GONE
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

    override fun onFavoriteClicked(gifData: GifData) {
        if (gifData.isFavorite)
            searchTrendingViewModel.removeFavoriteGif(gifData.id)
        else searchTrendingViewModel.insertFavoriteGif(gifData)
    }
}