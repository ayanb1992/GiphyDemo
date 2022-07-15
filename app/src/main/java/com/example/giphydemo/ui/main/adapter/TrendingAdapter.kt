package com.example.giphydemo.ui.main.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

class TrendingAdapter(private val context: Context, private var data: List<GifData>) :
    RecyclerView.Adapter<TrendingAdapter.ViewHolder>() {

    interface OnFavoriteClickListener {
        fun onFavoriteClicked(gifData: GifData)
    }

    private var onFavoriteClickListener: OnFavoriteClickListener? = null

    fun setOnFavoriteClickListener(onFavoriteClickListener: OnFavoriteClickListener) {
        this.onFavoriteClickListener = onFavoriteClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setGifData(data: List<GifData>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun getGifData(): List<GifData> {
        return data
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
            .listener(getGlideRequestListener(position, viewHolder))
            .into(viewHolder.gifView)

        viewHolder.markFavoriteBtn.setOnClickListener {
            if(!data[position].isFavorite) onFavoriteClickListener?.onFavoriteClicked(data[position])
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
                setFavoriteButton(data[position].isFavorite, viewHolder)
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

    private fun setFavoriteButton(favorite: Boolean, viewHolder: ViewHolder) {
        viewHolder.markFavoriteBtn.visibility = View.VISIBLE
        viewHolder.markFavoriteBtn.text =
            if (favorite)
                context.getString(R.string.remove_fav)
            else context.getString(R.string.add_fav)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var gifView: ImageView
        var markFavoriteBtn: Button
        var favIndicator: ImageView

        init {
            gifView = view.findViewById<View>(R.id.gifView) as ImageView
            markFavoriteBtn = view.findViewById(R.id.markFavoriteBtn) as Button
            favIndicator = view.findViewById(R.id.favIndicator) as ImageView
        }
    }
}