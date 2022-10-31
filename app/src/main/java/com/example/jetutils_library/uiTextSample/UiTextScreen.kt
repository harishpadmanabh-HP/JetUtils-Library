package com.example.jetutils_library.uiTextSample

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.jetutils_library.AppButton
import com.example.jetutils_library.AppCenteredColumn
import com.example.jetutils_library.R
import com.lib.jetutils.UiText.UiText
import com.lib.jetutils.UiText.showToast

val normalStringMessage = "Wow ! UiText is awesome !"

// Create UiText object with strings on runtime similar to normal strings.
val uiTextMessage = UiText.DynamicString(normalStringMessage)

/**
 * Create UiText object with strings from Res file. This helps in localising the strings for the whole App.
 * Context is no more required to get string from res file.
 */
val uiTextMessageWithResId = UiText.StringResId(R.string.uiTextSample)

@Composable
fun UiTextShowOff(
    navController: NavController
) {
    val context = LocalContext.current

    AppCenteredColumn {
        AppButton(text = "Toast with normal String", onClick = {
            toast(context, normalStringMessage)
        })
        AppButton(text = "Toast UiText with Dynamic String", onClick = {
            toast(context, uiTextMessage)
        })
        AppButton(text = "Toast UiText with resource String", onClick = {
            toast(context, uiTextMessageWithResId)
        })
    }
}

fun toast(context: Context, normalStringMessage: String) {
    /**Using @see [showToast] from library*/
    context.showToast(message = normalStringMessage)
}

fun toast(context: Context, uiText: UiText) {
    /**Using @see [showToast] from library*/
    context.showToast(uiText = uiText)
}
