package com.example.giphydemo.ui.main.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.giphydemo.R
import com.example.giphydemo.model.database.entity.FavoriteGifs
import com.example.giphydemo.util.Constants
import com.example.giphydemo.util.Constants.DEFAULT_HAPTIC_DURATION
import com.example.giphydemo.util.HapticHelper

class FavoritesAdapter(private val context: Context, private var data: ArrayList<FavoriteGifs>) :
    RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    interface OnFavoriteClickListener {
        fun onFavoriteClicked(gifData: FavoriteGifs)
    }

    private var onFavoriteClickListener: OnFavoriteClickListener? = null

    fun setOnFavoriteClickListener(onFavoriteClickListener: OnFavoriteClickListener) {
        this.onFavoriteClickListener = onFavoriteClickListener
    }

    fun setGifData(data: ArrayList<FavoriteGifs>) {
        val diffCallback = FavoritesCallback(this.data, data)
        val diffFavoriteGifs = DiffUtil.calculateDiff(diffCallback)
        this.data.clear()
        this.data.addAll(data)
        diffFavoriteGifs.dispatchUpdatesTo(this)
    }

    fun getGifData(): ArrayList<FavoriteGifs> {
        return data
    }

    fun clearGifData() {
        val diffCallback = FavoritesCallback(this.data, data)
        val diffFavoriteGifs = DiffUtil.calculateDiff(diffCallback)
        this.data.clear()
        diffFavoriteGifs.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.gif_view, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(data[position].url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(false)
            .transition(DrawableTransitionOptions.withCrossFade())
            .centerInside()
            .timeout(Constants.API_TIME_OUT.toInt())
            .listener(getGlideRequestListener(viewHolder.adapterPosition, viewHolder))
            .into(viewHolder.gifView)

        viewHolder.favIndicator.setOnClickListener {
            HapticHelper.vibrate(context, DEFAULT_HAPTIC_DURATION)
            onFavoriteClickListener?.onFavoriteClicked(data[position])
        }
    }

    private fun getGlideRequestListener(
        position: Int,
        viewHolder: ViewHolder
    ): RequestListener<Drawable> {
        return object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                setFavoriteIcon(data[position].isFavorite, viewHolder)
                return false
            }

        }
    }

    private fun setFavoriteIcon(favorite: Boolean, viewHolder: ViewHolder) {
        viewHolder.favIndicator.apply {
            val drawable =
                if (favorite) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
            setImageDrawable(ContextCompat.getDrawable(context, drawable))
            visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var gifView: ImageView = view.findViewById<View>(R.id.gifView) as ImageView
        var favIndicator: ImageButton = view.findViewById(R.id.favIndicator) as ImageButton
    }

    inner class FavoritesCallback(
        private val oldList: ArrayList<FavoriteGifs>,
        private val newList: ArrayList<FavoriteGifs>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id === newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldCourse: Int, newPosition: Int): Boolean {
            val (_, value, name) = oldList[oldCourse]
            val (_, value1, name1) = newList[newPosition]
            return name == name1 && value == value1
        }
    }
}