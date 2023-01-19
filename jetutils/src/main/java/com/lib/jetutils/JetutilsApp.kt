package com.lib.jetutils

import android.app.Application
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat

class JetutilsApp: Application() {
    override fun onCreate() {
        super.onCreate()
        val config = BundledEmojiCompatConfig(this)
        EmojiCompat.init(config)
    }
}