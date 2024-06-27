package com.android.mindmaster.Utility

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.RadioButton
import java.io.ByteArrayOutputStream

class CommonUtility {
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

    fun setupRadioListeners(
        sunRadioButton: RadioButton,
        monRadioButton: RadioButton,
        tueRadioButton: RadioButton,
        wedRadioButton: RadioButton,
        thuRadioButton: RadioButton,
        friRadioButton: RadioButton,
        satRadioButton: RadioButton,
        days: MutableList<String>
    ) {
        var isSundayChecked = false
        sunRadioButton.setOnClickListener {
            isSundayChecked = !isSundayChecked
            sunRadioButton.isChecked = isSundayChecked
            if (isSundayChecked) {
                days.add("Sunday")
            } else {
                days.remove("Sunday")
            }
        }

        var isMondayChecked = false
        monRadioButton.setOnClickListener {
            isMondayChecked = !isMondayChecked
            monRadioButton.isChecked = isMondayChecked
            if (isMondayChecked) {
                days.add("Monday")
            } else {
                days.remove("Monday")
            }
        }

        var isTuesdayChecked = false
        tueRadioButton.setOnClickListener {
            isTuesdayChecked = !isTuesdayChecked
            tueRadioButton.isChecked = isTuesdayChecked
            if (isTuesdayChecked) {
                days.add("Tuesday")
            } else {
                days.remove("Tuesday")
            }
        }

        var isWednesdayChecked = false
        wedRadioButton.setOnClickListener {
            isWednesdayChecked = !isWednesdayChecked
            wedRadioButton.isChecked = isWednesdayChecked
            if (isWednesdayChecked) {
                days.add("Wednesday")
            } else {
                days.remove("Wednesday")
            }
        }

        var isThursdayChecked = false
        thuRadioButton.setOnClickListener {
            isThursdayChecked = !isThursdayChecked
            thuRadioButton.isChecked = isThursdayChecked
            if (isThursdayChecked) {
                days.add("Thursday")
            } else {
                days.remove("Thursday")
            }
        }

        var isFridayChecked = false
        friRadioButton.setOnClickListener {
            isFridayChecked = !isFridayChecked
            friRadioButton.isChecked = isFridayChecked
            if (isFridayChecked) {
                days.add("Friday")
            } else {
                days.remove("Friday")
            }
        }

        var isSaturdayChecked = false
        satRadioButton.setOnClickListener {
            isSaturdayChecked = !isSaturdayChecked
            satRadioButton.isChecked = isSaturdayChecked
            if (isSaturdayChecked) {
                days.add("Saturday")
            } else {
                days.remove("Saturday")
            }
        }
    }
}