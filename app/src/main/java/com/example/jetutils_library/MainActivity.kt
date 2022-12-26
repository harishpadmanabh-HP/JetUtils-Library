package com.example.jetutils_library

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.jetutils_library.ui.theme.JetUtilsLibraryTheme
import com.lib.jetutils.UiText.showToast
import com.lib.jetutils.composeUtils.PermissionDialog
import java.util.jar.Manifest

private const val TAG = "MainActivity"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val visible = remember {
                mutableStateOf(true)
            }
            JetUtilsLibraryTheme {
                //MainNavGraph()
                PermissionDialog(visible = visible, permissions = arrayListOf(
                    android.Manifest.permission.READ_CONTACTS,
                    android.Manifest.permission.CAMERA
                )) {
                    Log.e(TAG, "onCreate: all permission enabled!", )
                }
            }
        }
    }
}

