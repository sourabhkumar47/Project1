package com.example.notesapp_internshala.Firebase

import android.util.Log
import android.widget.Toast
import com.example.notesapp_internshala.fragments.AddNotesFragment
import com.example.notesapp_internshala.fragments.EditNotesFragment
import com.example.notesapp_internshala.fragments.NotesFragment
import com.example.notesapp_internshala.models.Note
import com.example.notesapp_internshala.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun getCurrentUserId(): String {
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    

    fun createNote(fragment: AddNotesFragment, note: Note) {
        mFireStore.collection(Constants.NOTES)
            .document()
            .set(note, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(
                    fragment.requireContext(),
                    "Note Added Successfully",
                    Toast.LENGTH_LONG
                ).show()
                fragment.noteAddedSuccessfully()
            }
            .addOnFailureListener { execption ->

                Log.e(
                    fragment.javaClass.simpleName,
                    "Error while adding note",
                    execption
                )

                Toast.makeText(
                    fragment.requireContext(),
                    "Error while adding note",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    fun getNotesList(fragment: NotesFragment) {
        mFireStore.collection(Constants.NOTES)
            .whereEqualTo(Constants.CURRENT_USER_ID, getCurrentUserId())
            .orderBy(Constants.TIMESTAMP, Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                document ->
                val notesList: ArrayList<Note> = ArrayList()
                for (i in document.documents) {
                    val note = i.toObject(Note::class.java)!!
                    note.documentId = i.id
                    notesList.add(note)
                }

                fragment.setupUI(notesList)
            }
            .addOnFailureListener {e->
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName , "Error while loading Notes" , e)
                Log.d("TAG", "getNotesList: $e")
            }
    }


    fun deleteNote(fragment: NotesFragment, noteId: String) {
        mFireStore.collection(Constants.NOTES)
            .document(noteId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(fragment.context, "Note Deleted", Toast.LENGTH_LONG).show()
                fragment.noteDeletedSuccessfully()
            }
            .addOnFailureListener {
                fragment.hideProgressDialog()
                Toast.makeText(fragment.context, "Error while deleting the note", Toast.LENGTH_LONG).show()
            }
    }


}