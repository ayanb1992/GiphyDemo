package com.example.giphydemo.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.giphydemo.databinding.FragmentSearchTrendingBinding
import com.example.giphydemo.model.ErrorEntity
import com.example.giphydemo.model.GifData
import com.example.giphydemo.ui.main.adapter.TrendingAdapter
import com.example.giphydemo.ui.main.common.BaseFragment
import com.example.giphydemo.ui.main.common.PaginationScrollListener
import com.example.giphydemo.util.Constants.DEFAULT_HAPTIC_DURATION
import com.example.giphydemo.util.Constants.DEFAULT_SEARCH_GIF_OFFSET
import com.example.giphydemo.util.HapticHelper
import com.example.giphydemo.util.hideSoftKeyboard
import com.example.giphydemo.viewmodel.SearchTrendingViewModel


class SearchTrendingFragment : BaseFragment(), TrendingAdapter.OnFavoriteClickListener {

    private lateinit var searchTrendingViewModel: SearchTrendingViewModel
    private var binding: FragmentSearchTrendingBinding? = null
    private var adapter: TrendingAdapter? = null
    private var paginationScrollListener: PaginationScrollListener? = null

    companion object {
        @JvmStatic
        fun newInstance(): SearchTrendingFragment {
            return SearchTrendingFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchTrendingViewModel = ViewModelProvider(this)[SearchTrendingViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchTrendingBinding.inflate(inflater, container, false)
        val root = binding?.root
        setupResultList()
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

    private fun setupResultList() {
        context?.let { ctx ->
            val gifSearchResultView: RecyclerView = binding?.gifSearchResultView!!
            val layoutManager =
                LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false)
            gifSearchResultView.layoutManager = layoutManager
            gifSearchResultView.addItemDecoration(
                DividerItemDecoration(
                    context,
                    layoutManager.orientation
                )
            )
            createPaginationScrollListener(layoutManager)
            adapter = TrendingAdapter(ctx)
            (adapter as TrendingAdapter).setOnFavoriteClickListener(this@SearchTrendingFragment)
            gifSearchResultView.adapter = adapter
        }
    }

    private fun createPaginationScrollListener(layoutManager: LinearLayoutManager) {
        paginationScrollListener = object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return searchTrendingViewModel.isLastPage
            }

            override fun isLoading(): Boolean {
                return searchTrendingViewModel.isLoading
            }

            override fun loadMoreItems() {
                searchTrendingViewModel.isLoading = true
                binding?.progressbar?.visibility = View.VISIBLE
                getMoreItems()
            }
        }
    }

    fun getMoreItems() {
        if (searchTrendingViewModel.currentPage >= searchTrendingViewModel.totalPages) {
            searchTrendingViewModel.isLastPage = true
            binding?.progressbar?.visibility = View.GONE
        } else
            searchGifs(
                queryString = binding?.gifSearchEditText?.text?.toString() ?: "",
                offset = searchTrendingViewModel.currentPage * DEFAULT_SEARCH_GIF_OFFSET
            )
    }

    private fun searchGifs(queryString: String, offset: Int) {
        binding?.noFavFoundTv?.visibility = View.GONE
        searchTrendingViewModel.searchGifs(queryString, offset)
    }

    private fun setupObservers() {
        searchTrendingViewModel.gifsResponse.observe(viewLifecycleOwner) { gifResponse ->
            hideLoader()
            if (gifResponse.second.data.isNotEmpty()) {
                binding?.noFavFoundTv?.visibility = View.GONE
                if (gifResponse.first) {
                    if (searchTrendingViewModel.totalPages == 0)
                        searchTrendingViewModel.totalPages =
                            gifResponse.second.pagination.totalCount / DEFAULT_SEARCH_GIF_OFFSET
                    paginationScrollListener?.let {
                        binding?.gifSearchResultView?.addOnScrollListener(it)
                    }
                    binding?.progressbar?.visibility = View.GONE
                    if (searchTrendingViewModel.currentPage == 1) {
                        searchTrendingViewModel.dataList.apply {
                            clear()
                            addAll(gifResponse.second.data)
                        }
                        adapter?.setGifData(gifResponse.second.data)
                        searchTrendingViewModel.currentPage++
                    } else {
                        searchTrendingViewModel.isLoading = false
                        searchTrendingViewModel.dataList.apply {
                            addAll(gifResponse.second.data)
                        }
                        adapter?.addGifData(gifResponse.second.data)
                        searchTrendingViewModel.currentPage++
                    }
                } else {
                    paginationScrollListener?.let {
                        binding?.gifSearchResultView?.removeOnScrollListener(it)
                    }
                    searchTrendingViewModel.dataList.apply {
                        clear()
                        addAll(gifResponse.second.data)
                    }
                    adapter?.setGifData(gifResponse.second.data)
                }

            } else {
                binding?.noFavFoundTv?.visibility = View.VISIBLE
                searchTrendingViewModel.dataList.clear()
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

        searchTrendingViewModel.dbError.observe(viewLifecycleOwner) {
            if (it != null) {
                hideLoader()
                showErrorToast((it as ErrorEntity.DatabaseError).throwable?.localizedMessage ?: "")
                searchTrendingViewModel.dbError.value = null
            }
        }

        searchTrendingViewModel.networkError.observe(viewLifecycleOwner) {
            if (it != null) {
                hideLoader()
                when (it) {
                    is ErrorEntity.CustomError -> showErrorToast(
                        it.throwable?.localizedMessage ?: ""
                    )
                    is ErrorEntity.NetworkError -> showErrorToast(
                        it.throwable?.localizedMessage ?: ""
                    )
                    is ErrorEntity.APIError -> showErrorToast(it.throwable?.localizedMessage ?: "")
                    else -> showErrorToast()
                }
                searchTrendingViewModel.networkError.value = null
            }
        }
    }

    private fun setupListeners() {
        binding?.gifSearchImgButton?.setOnClickListener {
            context?.let { ctx -> HapticHelper.vibrate(ctx, DEFAULT_HAPTIC_DURATION) }
            context?.let { binding?.gifSearchEditText?.hideSoftKeyboard(it) }
            val queryString = binding?.gifSearchEditText?.text?.toString()
            if (queryString?.isNotEmpty() == true) {
                searchGifs(queryString)
            }
        }

        binding?.gifSearchView?.setEndIconOnClickListener {
            context?.let { ctx -> HapticHelper.vibrate(ctx, DEFAULT_HAPTIC_DURATION) }
            binding?.apply {
                gifSearchEditText.clearFocus()
                context?.let { binding?.gifSearchEditText?.hideSoftKeyboard(it) }
                if (gifSearchEditText.text?.isNotEmpty() == true) {
                    gifSearchEditText.setText("")
                    searchTrendingViewModel.dataList.clear()
                    adapter?.clearGifData()
                    loadTrendingGifsList()
                }
            }
        }

        binding?.gifSearchEditText?.addTextChangedListener {
            searchTrendingViewModel.queryString = it?.toString() ?: ""
        }
    }

    override fun onResume() {
        super.onResume()
        if (searchTrendingViewModel.queryString.isNotEmpty()) {
            binding?.gifSearchEditText?.setText(searchTrendingViewModel.queryString)
        }
        if (searchTrendingViewModel.dataList.isNotEmpty()) {
            adapter?.clearGifData()
            adapter?.setGifData(searchTrendingViewModel.dataList)
        } else {
            loadTrendingGifsList()
        }
    }

    private fun searchGifs(queryString: String) {
        showLoader()
        searchTrendingViewModel.dataList.clear()
        adapter?.clearGifData()
        searchTrendingViewModel.totalPages = 0
        searchTrendingViewModel.currentPage = 1
        binding?.noFavFoundTv?.visibility = View.GONE
        searchTrendingViewModel.searchGifs(queryString)
    }

    private fun loadTrendingGifsList() {
        showLoader()
        binding?.noFavFoundTv?.visibility = View.GONE
        searchTrendingViewModel.getTrendingGifs()
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

    override fun onPause() {
        super.onPause()
        HapticHelper.cancelVibration()
    }
}