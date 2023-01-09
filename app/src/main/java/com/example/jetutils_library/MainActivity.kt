package com.example.jetutils_library

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.jetutils_library.ui.theme.JetUtilsLibraryTheme

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            JetUtilsLibraryTheme {
                MainNavGraph()

            }
        }
    }
}



