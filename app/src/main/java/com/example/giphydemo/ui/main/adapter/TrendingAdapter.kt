package com.example.giphydemo.ui.main.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.giphydemo.R
import com.example.giphydemo.model.GifData

class TrendingAdapter(private val context: Context, private var data: List<GifData>) :
    RecyclerView.Adapter<TrendingAdapter.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setGifData(data: List<GifData>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.gif_view, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        Glide.with(context)
            .load(data[i].images.downsizedMedium?.url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(false)
            .into(viewHolder.gifView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var gifView: ImageView

        init {
            gifView = view.findViewById<View>(R.id.gif_view) as ImageView
        }
    }
}