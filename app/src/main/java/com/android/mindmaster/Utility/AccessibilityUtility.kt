package com.android.mindmaster.Utility

import android.content.Context
import android.provider.Settings
import android.text.TextUtils
import com.android.mindmaster.Service.WebService

class AccessibilityUtility {
    fun isAccessibilitySettingsOn(mContext: Context): Boolean {
        var accessibilityEnabled = 0
        val service = mContext.packageName + "/" + WebService::class.java.canonicalName
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                mContext.applicationContext.contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (ignored: Settings.SettingNotFoundException) { }

        val mStringColonSplitter = TextUtils.SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            val settingValue = Settings.Secure.getString(
                mContext.applicationContext.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue)
                while (mStringColonSplitter.hasNext()) {
                    val accessibilityService = mStringColonSplitter.next()
                    if (accessibilityService.equals(service, ignoreCase = true)) {
                        return true
                    }
                }
            }
        }

        return false
    }
}