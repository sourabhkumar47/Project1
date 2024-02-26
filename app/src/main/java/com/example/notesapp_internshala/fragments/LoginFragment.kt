package com.example.notesapp_internshala.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.notesapp_internshala.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LoginFragment : Fragment() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    companion object {
        private const val RC_SIGN_IN = 9001
    }
    lateinit var mProgressDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_login, container, false)



        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        val signInBtn: Button = rootView.findViewById(R.id.btnSignIn)
        signInBtn.setOnClickListener {
            signIn()
        }

        // Inflate the layout for this fragment
        return rootView
    }
    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
        showProgressDialog()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        hideProgressDialog()
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                showProgressDialog()
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.d("TAG", "onActivityResult: ${e.statusCode}")
                Toast.makeText(context, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    hideProgressDialog()
                    Toast.makeText(requireContext(), "Signed in as ${user?.displayName}", Toast.LENGTH_SHORT).show()
                    saveLoginState(true)
                    view?.findNavController()?.navigate(R.id.loginFragmentToNotesFragment)
                } else {
                    Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveLoginState(isLoggedIn: Boolean) {
        // Access SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        // Save the login state
        val editor = sharedPreferences.edit()
        editor.putBoolean("is_user_logged_in", isLoggedIn)
        editor.apply()
    }

    override fun onResume() {
        super.onResume()

        // Check the login state when the fragment is resumed
        checkLoginState()
    }

    private fun checkLoginState() {
        // Access SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        // Retrieve the login state
        val isUserLoggedIn = sharedPreferences.getBoolean("is_user_logged_in", false)

        if (isUserLoggedIn) {

            view?.findNavController()?.navigate(R.id.loginFragmentToNotesFragment)
        }
    }

    fun showProgressDialog() {

        mProgressDialog = Dialog(requireContext())
        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }
}

