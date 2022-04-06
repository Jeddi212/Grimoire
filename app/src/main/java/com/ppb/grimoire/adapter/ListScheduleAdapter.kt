package com.ppb.grimoire.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ppb.grimoire.databinding.ItemScheduleBinding
import com.ppb.grimoire.model.Schedule

class ListScheduleAdapter(
    private val listSchedule: ArrayList<Schedule>)
//    private val itemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<ListScheduleAdapter.ListViewHolder>() {

    class ListViewHolder(private val binding: ItemScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(schedule: Schedule /*, clickListener: OnItemClickListener*/) {
            with(binding){
                tvScheduleTitle.text = schedule.title
            }

//            itemView.setOnClickListener {
//                clickListener.onItemClicked(schedule)
//            }
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemScheduleBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
//        holder.bind(listSchedule[position], itemClickListener)
        holder.bind(listSchedule[position])
    }

    override fun getItemCount(): Int = listSchedule.size
}

interface OnItemClickListener{
    fun onItemClicked(schedule: Schedule)
}