package com.ppb.grimoire.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ppb.grimoire.CustomOnItemClickListener
import com.ppb.grimoire.ScheduleAddUpdateActivity
import com.ppb.grimoire.databinding.ItemScheduleBinding
import com.ppb.grimoire.model.Schedule

class ListScheduleAdapter(
    private val context: Fragment,
//    private val activity: Activity,
//    var listSchedule: ArrayList<Schedule>)
//    private val itemClickListener: OnItemClickListener)
    )
    : RecyclerView.Adapter<ListScheduleAdapter.ListViewHolder>() {

    var listSchedule = ArrayList<Schedule>()
    set(listSchedule) {
        if (listSchedule.size > 0) {
            this.listSchedule.clear()
            }
        this.listSchedule.addAll(listSchedule)

        notifyDataSetChanged()
    }

    inner class ListViewHolder(private val binding: ItemScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(schedule: Schedule /*, clickListener: OnItemClickListener*/) {
            with(binding) {
                tvScheduleTitle.text = schedule.title

                binding.tvScheduleTitle.setOnClickListener(CustomOnItemClickListener(
                    adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                    override fun onItemClicked(view: View, position: Int) {

                        // TODO activity disini nya mungkin
                        val intent = Intent(context.requireContext(), ScheduleAddUpdateActivity::class.java)

                        intent.putExtra(ScheduleAddUpdateActivity.EXTRA_POSITION, position)
                        intent.putExtra(ScheduleAddUpdateActivity.EXTRA_SCHEDULE, schedule)

                        // TODO disini errornyaaaaaa
                        context.startActivityForResult(intent, ScheduleAddUpdateActivity.REQUEST_UPDATE)

                    }
                }))
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

    // CRUD
    fun addItem(schedule: Schedule) {
        this.listSchedule.add(schedule)
        notifyItemInserted(this.listSchedule.size - 1)
    }

    fun updateItem(position: Int, schedule: Schedule) {
        this.listSchedule[position] = schedule
        notifyItemChanged(position, schedule)
    }

    fun removeItem(position: Int) {
        this.listSchedule.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listSchedule.size)
    }
}

interface OnItemClickListener{
    fun onItemClicked(schedule: Schedule)
}