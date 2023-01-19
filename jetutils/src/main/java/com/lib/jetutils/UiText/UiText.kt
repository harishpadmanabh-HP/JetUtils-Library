package com.lib.jetutils.UiText

import android.content.Context
import android.widget.Toast

/**
 *# Ui Text
 *
 * This class helps for using both strings and resource strings from xml file with out using context to get string with resource ids.
 * This class is very useful when it is needed to localize the strings when its used in a normal class where there is no reach for context.
 * To pass this text to a toast @see [showToast]
 */
sealed class UiText() {
    data class DynamicString(val message: String?) : UiText()
    data class StringResId(val resID: Int?) : UiText()
}

/**
 *
 * Useful and customizable extension function to show toast all over the app with a context.
 * This function works with either non null uiText or non null message passed to this function and called with  a non null context.
 * @param uiText UiText Instance  can be a normal string or string from Res file
 * @param message Normal string
 * @param duration Int : Toast duration for toast message
 */
fun Context.showToast(
    uiText: UiText? = null,
    message: String? = null,
    duration: Int = Toast.LENGTH_SHORT
) {
    if (uiText != null) {
        when (uiText) {
            is UiText.DynamicString -> {
                if (!uiText.message.isNullOrEmpty())
                    Toast.makeText(this, uiText.message, duration).show()
            }
            is UiText.StringResId -> {
                if (uiText.resID != null)
                    Toast.makeText(this, this.getString(uiText.resID), duration).show()
            }
        }
    } else if (!message.isNullOrEmpty()) {
        Toast.makeText(this, message, duration).show()
    }

}