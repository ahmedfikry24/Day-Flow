package com.example.dayflow.utils

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri

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
                runCatching {
                    mediaPlayer?.release()
                    mediaPlayer = MediaPlayer().apply {
                        setDataSource(context, uri)
                        isLooping = true
                        prepare()
                        start()
                    }
                }.onFailure { playDefaultAlarmRingtone(context) }
            }
        } else playDefaultAlarmRingtone(context)
    }

    fun playDefaultAlarmRingtone(context: Context) {
        ringtone = RingtoneManager.getRingtone(
            context,
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        )
        ringtone?.play()
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
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)
    }
}