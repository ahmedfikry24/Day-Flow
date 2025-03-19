package com.example.dayflow.utils

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import java.io.IOException

object MediaPlayerManager {
    private var mediaPlayer: MediaPlayer? = null
    private var ringtone: Ringtone? = null

    fun startAlarmRingtone(context: Context, uri: Uri?) {
        setVolumeToMax(context)

        if (uri != null) {
            if (uri.toString().startsWith("content://media/internal/audio/media")) {
                ringtone = RingtoneManager.getRingtone(context, uri)
                ringtone?.play()
            } else {
                mediaPlayer?.release()
                mediaPlayer = MediaPlayer().apply {
                    try {
                        setDataSource(context, uri)
                        isLooping = true
                        setVolume(1f, 1f)
                        prepare()
                        start()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        } else {
            ringtone = RingtoneManager.getRingtone(
                context,
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            )
            ringtone?.play()
        }
    }

    fun stopAlarmRingtone() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        ringtone?.stop()
        mediaPlayer = null
        ringtone = null
    }

    private fun setVolumeToMax(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val threshold = (maxVolume * 0.5).toInt()
        if (currentVolume < threshold) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)
        }
    }
}