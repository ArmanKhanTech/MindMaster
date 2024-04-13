package com.android.achievix.Utility

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.io.ByteArrayOutputStream

class CommonUtil {
    fun compressIcon(icon: Drawable, context: Context): BitmapDrawable {
        val appIcon: Drawable = icon
        val bitmap = Bitmap.createBitmap(
            appIcon.intrinsicWidth,
            appIcon.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)

        appIcon.setBounds(0, 0, canvas.width, canvas.height)
        appIcon.draw(canvas)

        var byteArray: ByteArray

        ByteArrayOutputStream().use { stream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            byteArray = stream.toByteArray()
        }

        return BitmapDrawable(
            context.resources,
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        )
    }
}