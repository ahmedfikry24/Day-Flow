package com.example.dayflow.data.utils

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

object MediaPlayerManager {
    private var mediaPlayer: MediaPlayer? = null

    fun startAlarmRingtone(context: Context, ringtone: Uri?) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, ringtone).apply {
                isLooping = true
                start()
            }
        }
    }

    fun stopAlarmRingtone() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}