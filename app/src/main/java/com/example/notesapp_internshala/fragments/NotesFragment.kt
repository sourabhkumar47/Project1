package com.example.notesapp_internshala.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp_internshala.Firebase.FirestoreClass
import com.example.notesapp_internshala.R
import com.example.notesapp_internshala.adapter.ItemNotesAdapter
import com.example.notesapp_internshala.databinding.FragmentNotesBinding
import com.example.notesapp_internshala.models.Note
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


class NotesFragment : Fragment() {

    private var binding: FragmentNotesBinding? = null
    private val _binding get() = binding!!
    private lateinit var adapter: ItemNotesAdapter
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    lateinit var mProgressDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNotesBinding.inflate(inflater, container, false)
        val rootView = _binding.root

        val addNotesBtn: CardView = rootView.findViewById(R.id.addNoteBtn)
        addNotesBtn.setOnClickListener {
            view?.findNavController()?.navigate(R.id.NotesFragmentToAddNotesFragment)
        }

        binding!!.settingsIcon.setOnClickListener {
            showPopup(it)
        }

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        binding!!.userName.text = "Welcome"

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)


        showProgressDialog()
        FirestoreClass().getNotesList(this)

        return rootView
    }



    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun showPopup(v : View){
        val popup = PopupMenu(requireContext(), v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_sign_out, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.action_sign_out -> {
                    showProgressDialog()
                    signOutAndStartSignInActivity()
                }
            }
            true
        }
        popup.show()
    }

    private fun signOutAndStartSignInActivity() {
        mAuth.signOut()

        mGoogleSignInClient.signOut().addOnCompleteListener(requireActivity()) {
            clearLoginState()
            view?.findNavController()?.navigate(R.id.NotesFragmentToLoginFragment)
            hideProgressDialog()
        }
    }

    private fun clearLoginState() {
        // Access SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        // Clear the login state
        val editor = sharedPreferences.edit()
        editor.putBoolean("is_user_logged_in", false)
        editor.apply()
    }

    fun showProgressDialog() {

        mProgressDialog = Dialog(requireContext())
        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

    fun setupUI(notesList: ArrayList<Note>) {
        hideProgressDialog()
        if(notesList.size > 0) {
            binding?.rvVisibleNotes?.visibility = View.VISIBLE
            binding?.tvNoNotesAdded?.visibility = View.GONE
            binding?.rvVisibleNotes?.layoutManager = LinearLayoutManager(context)
            binding?.rvVisibleNotes?.setHasFixedSize(true)

            adapter = ItemNotesAdapter(notesList)
            binding?.rvVisibleNotes?.adapter = adapter

            adapter.setOnClickListener(object :
                ItemNotesAdapter.OnNoteItemClickListener{
                override fun onEditClick(position: Int,  note: Note) {
                    val bundle = Bundle().apply {
                                putParcelable("note" , note)
                            }

                            val editNoteFragment = EditNotesFragment()
                            editNoteFragment.arguments = bundle

                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.nav_host_fragment, editNoteFragment)
                    transaction.addToBackStack(null) // This allows navigating back to the NotesFragment
                    transaction.commit()
                }

                override fun onDeleteClick(position: Int, noteId: String) {
                    showProgressDialog()
                    FirestoreClass().deleteNote(this@NotesFragment, noteId)
                    adapter.notifyItemRemoved(position)
                    FirestoreClass().getNotesList(this@NotesFragment)
                }

            })

        } else {
            binding?.rvVisibleNotes?.visibility = View.GONE
            binding?.tvNoNotesAdded?.visibility = View.VISIBLE
        }
    }

    fun noteDeletedSuccessfully() {
        hideProgressDialog()
    }

}