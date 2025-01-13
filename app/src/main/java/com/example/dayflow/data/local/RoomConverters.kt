package com.example.dayflow.data.local

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class RoomConverters {
    @TypeConverter
    fun converterStringToBitmap(encodedString: String): Bitmap? {
        return try {
            val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @TypeConverter
    fun converterBitmapToString(bitmap: Bitmap): String {
        val byteArrayOutput = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, byteArrayOutput)
        val byteArray = byteArrayOutput.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

}


