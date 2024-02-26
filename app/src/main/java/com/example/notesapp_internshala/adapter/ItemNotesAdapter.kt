package com.example.notesapp_internshala.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp_internshala.databinding.ItemsNotesBinding
import com.example.notesapp_internshala.models.Note

class ItemNotesAdapter(private val list: ArrayList<Note>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listener: OnNoteItemClickListener? = null


    class MyViewHolder(binding: ItemsNotesBinding): RecyclerView.ViewHolder(binding.root) {
        val tvNoteTitle = binding.notesTitle
        val tvNoteDescription = binding.notesBody
        val ibEditNote = binding.editNotes
        val ibDeleteNote = binding.ibDeleteList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(ItemsNotesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if(holder is MyViewHolder) {
            holder.tvNoteTitle.text = model.title
            holder.tvNoteDescription.text = model.description

            holder.ibDeleteNote.setOnClickListener {
                if(listener != null) {
                    listener!!.onDeleteClick(position, model.documentId)
                }
            }

            holder.ibEditNote.setOnClickListener {
                if(listener != null) {
                    listener!!.onEditClick(position,  list[position])
                }
            }
        }
    }

    fun setOnClickListener(listener: OnNoteItemClickListener) {
        this.listener = listener
    }

    interface OnNoteItemClickListener {
        fun onEditClick(position: Int,  note: Note)
        fun onDeleteClick(position: Int, noteId: String )
    }


}