package com.lib.jetutils.utils

import android.os.Build

inline fun <T> sdk29AndUp(block: () -> T): T? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        return block()
    }
    return null
}