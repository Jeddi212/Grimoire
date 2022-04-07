package com.ppb.grimoire

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ppb.grimoire.adapter.News
import com.ppb.grimoire.databinding.ItemNewsBinding

class ListNewsAdapter(private val listNews: ArrayList<News>, val itemClickListener: OnItemClickListener): RecyclerView.Adapter<ListNewsAdapter.ListViewHolder>(){
    inner class ListViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: News, clickListener: OnItemClickListener) {
            with(binding){
                Glide.with(itemView.context)
                    .load(news.photo)
                    .apply(RequestOptions().override(55, 55))
                    .into(imgItemPhoto)

                tvItemName.text = news.title
                tvItemDescription.text = news.description
            }
            itemView.setOnClickListener {
                clickListener.onItemClicked(news)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listNews[position], itemClickListener)

    }

    override fun getItemCount(): Int {
        return listNews.size
    }
}

interface OnItemClickListener{
    fun onItemClicked(news: News) {

    }
}