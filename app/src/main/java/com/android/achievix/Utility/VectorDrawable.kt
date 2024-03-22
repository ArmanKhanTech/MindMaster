package com.android.achievix.Utility

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

object VectorDrawable {
    private fun getDrawable(context: Context, drawableResId: Int): Drawable? {
        return VectorDrawableCompat.create(context.resources, drawableResId, context.theme)
    }

    fun getDrawable(context: Context, drawableResId: Int, colorFilter: Int): Drawable {
        val drawable = getDrawable(context, drawableResId)
        drawable!!.setColorFilter(colorFilter, PorterDuff.Mode.SRC_IN)
        return drawable
    }
}