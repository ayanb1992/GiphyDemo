package com.example.giphydemo.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.giphydemo.databinding.FragmentFavoritesBinding
import com.example.giphydemo.model.ErrorEntity
import com.example.giphydemo.model.database.entity.FavoriteGifs
import com.example.giphydemo.ui.main.adapter.FavoritesAdapter
import com.example.giphydemo.ui.main.common.BaseFragment
import com.example.giphydemo.viewmodel.SearchTrendingViewModel

class FavoritesFragment : BaseFragment(), FavoritesAdapter.OnFavoriteClickListener {
    private lateinit var pageViewModel: SearchTrendingViewModel
    private var _binding: FragmentFavoritesBinding? = null

    private val binding get() = _binding!!
    private var adapter: FavoritesAdapter? = null

    companion object {
        const val COLUMN_COUNT = 2

        @JvmStatic
        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this)[SearchTrendingViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val root = binding.root
        setupObservers()
        return root
    }

    private fun showLoader() {
        binding.loaderLayout.root.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        binding.loaderLayout.root.visibility = View.GONE
    }

    private fun setupObservers() {
        pageViewModel.allFavoriteGifs.observe(viewLifecycleOwner) {
            hideLoader()
            if (it.isNotEmpty()) {
                binding.noFavFoundTv.visibility = View.GONE
                val favoritesResultList = binding.favoritesResultList
                favoritesResultList.layoutManager =
                    GridLayoutManager(requireContext(), COLUMN_COUNT)
                if (adapter == null) {
                    adapter = FavoritesAdapter(requireContext(), (it as ArrayList))
                    (adapter as FavoritesAdapter).setOnFavoriteClickListener(this@FavoritesFragment)
                    favoritesResultList.adapter = adapter
                } else {
                    adapter?.setGifData(it)
                }
            } else {
                adapter?.clearGifData()
                binding.noFavFoundTv.visibility = View.VISIBLE
            }
        }

        pageViewModel.removeComplete.observe(viewLifecycleOwner) {
            if (it != null && it.first) {
                val list = ArrayList<FavoriteGifs>()
                list.apply {
                    addAll(adapter?.getGifData() ?: emptyList())
                    removeIf { item -> item.id == it.second }
                    adapter?.setGifData(this)
                    if (this.isEmpty()) binding.noFavFoundTv.visibility = View.VISIBLE
                }
            }
        }

        pageViewModel.dbError.observe(viewLifecycleOwner) {
            if (it != null) {
                hideLoader()
                showErrorToast((it as ErrorEntity.DatabaseError).throwable?.localizedMessage ?: "")
                pageViewModel.dbError.value = null
            }
        }

        pageViewModel.networkError.observe(viewLifecycleOwner) {
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
                pageViewModel.networkError.value = null
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showLoader()
        pageViewModel.fetchAllFavoriteGifs()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onFavoriteClicked(gifData: FavoriteGifs) {
        if (gifData.isFavorite) pageViewModel.removeFavoriteGif(gifData.id)
    }
}