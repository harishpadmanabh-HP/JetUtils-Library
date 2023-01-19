package com.lib.jetutils.utils

import androidx.emoji.text.EmojiCompat


fun String.containsIn(searchQuery: String, replaceChar: String = " "): Boolean {
    return this.asSearchFilter(replaceChar).contains(searchQuery.asSearchFilter(replaceChar))
}

private fun String.asSearchFilter(replaceChar: String = " "): String {
    return this.replace(replaceChar, "").lowercase()
}

fun String.toEmoji(): String {
    var selectedEmoji: String = try {
        EmojiCompat.get().process(CountryCodeUtil.convertToEmoji(this.uppercase()))
            .toString()
    } catch (e: Exception) {
        ""
    }
    return selectedEmoji
}