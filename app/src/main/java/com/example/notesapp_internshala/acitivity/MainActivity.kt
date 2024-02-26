package com.example.notesapp_internshala.acitivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.notesapp_internshala.R
import com.example.notesapp_internshala.fragments.NotesFragment

class MainActivity : AppCompatActivity() {
    private var backPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController: NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navController.setGraph(R.navigation.nav_graph)
    }

    override fun onBackPressed() {
        // Check if the current fragment is NotesFragment
        val currentFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        if (currentFragment is NotesFragment) {
            if (backPressedOnce) {
                // Exit the application
                super.onBackPressed()
            } else {
                // Show a message or perform any other action
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
                backPressedOnce = true

                // Reset the flag after a certain delay (e.g., 2 seconds)
                Handler().postDelayed({
                    backPressedOnce = false
                }, 2000)
            }
        } else {
            // If not in NotesFragment, proceed with the default behavior
            super.onBackPressed()
        }
    }



}