package com.example.dayflow.data.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class ReBootReceiver : BroadcastReceiver() {
    override fun onReceive(con: Context?, inten: Intent?) {
        inten?.let { intent ->
            if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
                con?.let { context ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

                    }else{

                    }
                }
            }
        }
    }
}
