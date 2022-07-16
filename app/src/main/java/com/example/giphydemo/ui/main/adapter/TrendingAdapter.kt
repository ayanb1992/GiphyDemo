package com.example.giphydemo.ui.main.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.giphydemo.R
import com.example.giphydemo.model.GifData

class TrendingAdapter(private val context: Context, private var data: ArrayList<GifData>) :
    RecyclerView.Adapter<TrendingAdapter.ViewHolder>() {

    interface OnFavoriteClickListener {
        fun onFavoriteClicked(gifData: GifData)
    }

    private var onFavoriteClickListener: OnFavoriteClickListener? = null

    fun setOnFavoriteClickListener(onFavoriteClickListener: OnFavoriteClickListener) {
        this.onFavoriteClickListener = onFavoriteClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setGifData(data: ArrayList<GifData>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun getGifData(): ArrayList<GifData> {
        return data
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearGifData() {
        this.data.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.gif_view, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(data[position].images.downsizedMedium?.url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(false)
            .transition(DrawableTransitionOptions.withCrossFade())
            .centerInside()
            .listener(getGlideRequestListener(position, viewHolder))
            .into(viewHolder.gifView)

        viewHolder.favIndicator.setOnClickListener {
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

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var gifView: ImageView
        var favIndicator: ImageButton

        init {
            gifView = view.findViewById<View>(R.id.gifView) as ImageView
            favIndicator = view.findViewById(R.id.favIndicator) as ImageButton
        }
    }
}