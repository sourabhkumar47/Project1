package com.example.notesapp_internshala.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.notesapp_internshala.Firebase.FirestoreClass
import com.example.notesapp_internshala.R
import com.example.notesapp_internshala.models.Note
import com.example.notesapp_internshala.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore


class EditNotesFragment : Fragment() {

    companion object {
        const val ARG_NOTE = "note"
    }
    lateinit var mProgressDialog: Dialog
    private val mFireStore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView =  inflater.inflate(R.layout.fragment_edit_notes, container, false)
        val note = arguments?.getParcelable<Note>(ARG_NOTE)

        // Accessing title and description of notes
        val etEditNotesTitle: TextView = rootView.findViewById(R.id.etEditNotesTitle)
        val etEditNotesDescription: TextView = rootView.findViewById(R.id.etEditNotesBody)
        if (note != null) {
            etEditNotesTitle.text = note.title
            etEditNotesDescription.text = note.description
        }


        val tvEditBtn: TextView = rootView.findViewById(R.id.tvEditBtn)
        tvEditBtn.setOnClickListener{
            if(etEditNotesTitle.text.isNullOrBlank()) {
                Toast.makeText(context,
                    "Please enter the Notes Title",
                    Toast.LENGTH_LONG).show()
            } else {

                showProgressDialog()
                Log.d("beforeDocumentId", "documentId: ${note!!.documentId}")
                val updateHashMap = HashMap<String , Any>()
                updateHashMap["title"] = etEditNotesTitle.text.toString()
                updateHashMap["description"] = etEditNotesDescription.text.toString()


                mFireStore.collection(Constants.NOTES)
                    .document(note.documentId)
                    .update(updateHashMap)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Note updated successfully", Toast.LENGTH_LONG).show()
                        updateNoteSuccessfully()
                    }

                    .addOnFailureListener {exception->
                        Log.d("TAG", "updateNote: $exception")
                        hideProgressDialog()
                        Toast.makeText(context, "Error while updating the note", Toast.LENGTH_LONG).show()
                    }

            }
        }

        val closeBtn: ImageButton = rootView.findViewById(R.id.editCloseBtn)
        closeBtn.setOnClickListener {
            fragmentManager?.popBackStack()
        }

        return rootView
    }


    fun showProgressDialog() {

        mProgressDialog = Dialog(requireContext())
        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

    fun updateNoteSuccessfully() {
        hideProgressDialog()
        Log.d("SUCCESS", "updateNoteSuccessfully")
        fragmentManager?.popBackStack()
    }



}