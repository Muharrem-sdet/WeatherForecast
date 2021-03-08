package ustun.muharrem.weatherforecast.utilities

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

object SharedPrefs {

    private fun getSharedPref(activity: Activity): SharedPreferences {
        return activity.getPreferences(Context.MODE_PRIVATE)
    }

    fun getIsCelsiusFromSettings(activity: Activity): Boolean {
        return getSharedPref(activity).getBoolean(IS_CELSIUS_SETTING_KEY, IS_CELSIUS_DEFAULT_VALUE)
    }

    fun setIsCelsiusInSettings(activity: Activity, value: Boolean) {
        val editor = getSharedPref(activity).edit()
        editor.putBoolean(IS_CELSIUS_SETTING_KEY, value)
        editor.apply()
    }

    fun setNumberOfDays(activity: Activity, days: String) {
        val editor = getSharedPref(activity).edit()
        editor.putString(NUMBER_OF_DAYS_SETTING_KEY, days)
        editor.apply()
    }

    fun getNumberOfDays(activity: Activity): String? {
        return getSharedPref(activity).getString(NUMBER_OF_DAYS_SETTING_KEY, "16")
    }

    fun getNotificationsSettings(activity: Activity): Boolean {
        return getSharedPref(activity).getBoolean(IS_NOTIFICATIONS_ENABLED, true)
    }

    fun setNotificationsSettings(activity: Activity, isEnabled: Boolean) {
        getSharedPref(activity).edit().putBoolean(IS_NOTIFICATIONS_ENABLED, isEnabled).apply()
    }

    fun getLangCode(activity: Activity): String? {
        return getSharedPref(activity).getString(LANG_CODE_SETTINGS_KEY, null)
    }

    fun setLangCode(activity: Activity, langCode: String) {
        val editor = getSharedPref(activity).edit()
        editor.putString(LANG_CODE_SETTINGS_KEY, langCode)
        editor.apply()
    }

}