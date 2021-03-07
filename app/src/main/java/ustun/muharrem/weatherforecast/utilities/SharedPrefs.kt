package ustun.muharrem.weatherforecast.utilities

import android.app.Activity
import android.content.Context

object SharedPrefs {
    fun getIsCelsiusFromSettings(activity: Activity): Boolean {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getBoolean(IS_METRIC_SETTING_KEY, true)
    }

    fun setIsCelsiusInSettings(activity: Activity, value: Boolean) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(IS_METRIC_SETTING_KEY, value)
        editor.apply()
    }

    fun setNumberOfDays(activity: Activity, days: String) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(NUMBER_OF_DAYS_SETTING_KEY, days)
        editor.apply()
    }

    fun getNumberOfDays(activity: Activity): String? {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getString(NUMBER_OF_DAYS_SETTING_KEY, "16")
    }

    fun getNotificationsSettings(activity: Activity): Boolean {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getBoolean(IS_NOTIFICATIONS_ENABLED, true)
    }

    fun setNotificationsSettings(activity: Activity, isEnabled: Boolean) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean(IS_NOTIFICATIONS_ENABLED, isEnabled).apply()
    }

    fun getLangCode(activity: Activity): String? {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getString(LANG_CODE_SETTINGS_KEY, null)
    }

    fun setLangCode(activity: Activity, langCode: String) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(LANG_CODE_SETTINGS_KEY, langCode)
        editor.apply()
    }

}