package com.lib.jetutils.composeUtils

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.input.TextFieldValue

private val osProperty: String = System.getProperty("os.name", "unknown") as String
private val isMac = osProperty.startsWith("mac", ignoreCase = true)


private fun TextFieldValue.isCursorAtTheEnd(): Boolean {
    val hasNoSelection = selection.collapsed
    val isCursorAtTheEnd = text.length == selection.end
    return hasNoSelection && isCursorAtTheEnd
}

@OptIn(ExperimentalComposeUiApi::class)
private fun androidx.compose.ui.input.key.KeyEvent.isCtrlBackspace() =
    (key == Key.Backspace || key == Key.Enter || key == Key.Delete
            || key == Key.DirectionUp || key == Key.DirectionDown
            || key == Key.DirectionLeft || key == Key.DirectionRight) && ((isCtrlPressed || isAltPressed) || (isMac && isAltPressed))

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.persistShortKeyForInput(input: TextFieldValue): Modifier {
    return onPreviewKeyEvent { event ->
        // Returns true to mark the event as handled and stop its propagation.
        when {
            // Temporary fix for https://github.com/JetBrains/compose-jb/issues/565.
            // To repro, press CTRL/Option+backspace on an empty TextField.
            event.isCtrlBackspace() && input.text.isEmpty() -> true
            // Temporary fix for https://github.com/JetBrains/compose-jb/issues/2023.
            // To repro, simply press the DEL key at the end of the text.
            event.key == Key.Delete && input.isCursorAtTheEnd() -> true
            else -> false
        }
    }
}