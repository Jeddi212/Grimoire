package com.ppb.grimoire.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ppb.grimoire.CustomOnItemClickListener
import com.ppb.grimoire.NoteAddUpdateActivity
import com.ppb.grimoire.R
import com.ppb.grimoire.databinding.ItemNewsBinding
import com.ppb.grimoire.databinding.ItemNoteBinding
import com.ppb.grimoire.model.Note
//import kotlinx.android.synthetic.main.item_note.view.*

class NoteAdapter(private val context: Fragment,) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    var listNotes = ArrayList<Note>()
        set(listNotes) {
            if (listNotes.size > 0) {
                this.listNotes.clear()
            }
            this.listNotes.addAll(listNotes)

            notifyDataSetChanged()
        }

    fun addItem(note: Note) {
        this.listNotes.add(note)
        notifyItemInserted(this.listNotes.size - 1)
    }

    fun updateItem(position: Int, note: Note) {
        this.listNotes[position] = note
        notifyItemChanged(position, note)
    }

    fun removeItem(position: Int) {
        this.listNotes.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listNotes.size)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(listNotes[position])
    }

    override fun getItemCount(): Int = this.listNotes.size

    inner class NoteViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            with(binding){
                tvItemTitle.text = note.title
                tvItemDate.text = note.date
                tvItemDescription.text = note.description

                cvItemNote.setOnClickListener(CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                    override fun onItemClicked(view: View, position: Int) {
                        val intent = Intent(context.requireContext(), NoteAddUpdateActivity::class.java)
                        intent.putExtra(NoteAddUpdateActivity.EXTRA_POSITION, position)
                        intent.putExtra(NoteAddUpdateActivity.EXTRA_NOTE, note)
                        context.startActivityForResult(intent, NoteAddUpdateActivity.REQUEST_UPDATE)
                    }
                }))
            }
        }
    }
}