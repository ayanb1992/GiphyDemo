package com.example.giphydemo.ui.main.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.giphydemo.util.hideSoftKeyboard
import com.example.giphydemo.viewmodel.SearchTrendingViewModel


class SearchTrendingFragment : BaseFragment(), TrendingAdapter.OnFavoriteClickListener {

    private lateinit var searchTrendingViewModel: SearchTrendingViewModel
    private var binding: FragmentSearchTrendingBinding? = null
    private var adapter: TrendingAdapter? = null
    private var paginationScrollListener: PaginationScrollListener? = null

    private var isLastPage: Boolean = false
    private var isLoading: Boolean = false
    private var totalPages: Int = 0
    private var currentPage: Int = 1

    companion object {
        private const val DEFAULT_OFFSET = 25

        @JvmStatic
        fun newInstance(): SearchTrendingFragment {
            return SearchTrendingFragment()
        }
    }

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
        val gifSearchResultView: RecyclerView = binding?.gifSearchResultView!!
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        gifSearchResultView.layoutManager = layoutManager
        gifSearchResultView.addItemDecoration(
            DividerItemDecoration(
                context,
                layoutManager.orientation
            )
        )
        createPaginationScrollListener(layoutManager)
        adapter = TrendingAdapter(requireContext())
        (adapter as TrendingAdapter).setOnFavoriteClickListener(this@SearchTrendingFragment)
        gifSearchResultView.adapter = adapter
    }

    private fun createPaginationScrollListener(layoutManager: LinearLayoutManager) {
        paginationScrollListener = object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                binding?.progressbar?.visibility = View.VISIBLE
                getMoreItems()
            }
        }
    }

    fun getMoreItems() {
        if (currentPage >= totalPages) {
            isLastPage = true
            binding?.progressbar?.visibility = View.GONE
        } else
            searchGifs(
                queryString = binding?.gifSearchEditText?.text?.toString() ?: "",
                offset = currentPage * DEFAULT_OFFSET
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
                    if (totalPages == 0)
                        totalPages = gifResponse.second.pagination.totalCount / DEFAULT_OFFSET
                    paginationScrollListener?.let {
                        binding?.gifSearchResultView?.addOnScrollListener(it)
                    }
                    binding?.progressbar?.visibility = View.GONE
                    if (currentPage == 1) {
                        adapter?.setGifData(gifResponse.second.data)
                        currentPage++
                    } else {
                        isLoading = false
                        adapter?.addGifData(gifResponse.second.data)
                        currentPage++
                    }
                } else {
                    paginationScrollListener?.let {
                        binding?.gifSearchResultView?.removeOnScrollListener(it)
                    }
                    adapter?.setGifData(gifResponse.second.data)
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
                    adapter?.clearGifData()
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
        if (adapter?.getGifData()?.isEmpty() == false
            && binding?.gifSearchEditText?.text?.toString()?.isEmpty() == true
        ) {
            adapter?.clearGifData()
            loadTrendingGifsList()
        } else if (adapter?.getGifData()?.isEmpty() == false
            && binding?.gifSearchEditText?.text?.toString()?.isNotEmpty() == true
        ) {
            searchGifs(binding?.gifSearchEditText?.text?.toString() ?: "")
        }
    }

    private fun searchGifs(queryString: String) {
        showLoader()
        adapter?.clearGifData()
        totalPages = 0
        currentPage = 1
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
}