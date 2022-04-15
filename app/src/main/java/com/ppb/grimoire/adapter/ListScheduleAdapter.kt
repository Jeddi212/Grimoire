package com.ppb.grimoire.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
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
) : RecyclerView.Adapter<ListScheduleAdapter.ListViewHolder>() {

    var listSchedule = ArrayList<Schedule>()
        @SuppressLint("NotifyDataSetChanged")
        set(listSchedule) {
            if (listSchedule.size > 0) {
                this.listSchedule.clear()
            }
            this.listSchedule.addAll(listSchedule)

            notifyDataSetChanged()
        }

    inner class ListViewHolder(private val binding: ItemScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(schedule: Schedule) {
            with(binding) {
                tvScheduleTitle.text = schedule.title

                binding.tvScheduleTitle.setOnClickListener(
                    CustomOnItemClickListener(
                        adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                            override fun onItemClicked(view: View, position: Int) {

                                val intent = Intent(
                                    context.requireContext(),
                                    ScheduleAddUpdateActivity::class.java
                                )

                                intent.putExtra(ScheduleAddUpdateActivity.EXTRA_POSITION, position)
                                intent.putExtra(ScheduleAddUpdateActivity.EXTRA_SCHEDULE, schedule)

                                context.startActivityForResult(
                                    intent,
                                    ScheduleAddUpdateActivity.REQUEST_UPDATE
                                )

                            }
                        })
                )
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemScheduleBinding
            .inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder
            .bind(
                listSchedule[position]
            )
    }

    override fun getItemCount(): Int = listSchedule.size

    fun addItem(schedule: Schedule) {
        this.listSchedule.add(schedule)
        notifyItemInserted(listSchedule.size - 1)
    }

    fun updateItem(position: Int, schedule: Schedule) {
        this.listSchedule[position] = schedule
        notifyItemChanged(position, schedule)
    }

    fun removeItem(position: Int) {
        this.listSchedule.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, listSchedule.size)
    }
}
