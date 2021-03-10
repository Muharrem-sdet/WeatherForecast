package ustun.muharrem.weatherforecast.utilities

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

object SharedPrefs {

    var application = Application()

    private fun getSharedPref(): SharedPreferences {
        return application.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun getIsCelsiusFromSettings(): Boolean {
        return getSharedPref().getBoolean(IS_CELSIUS_SETTING_KEY, IS_CELSIUS_DEFAULT_VALUE)
    }

    fun setIsCelsiusInSettings(value: Boolean) {
        val editor = getSharedPref().edit()
        editor.putBoolean(IS_CELSIUS_SETTING_KEY, value)
        editor.apply()
    }

    fun setNumberOfDays(days: String) {
        val editor = getSharedPref().edit()
        editor.putString(NUMBER_OF_DAYS_SETTING_KEY, days)
        editor.apply()
    }

    fun getNumberOfDays(): String? {
        return getSharedPref().getString(NUMBER_OF_DAYS_SETTING_KEY, "16")
    }

    fun getNotificationsSettings(): Boolean {
        return getSharedPref().getBoolean(IS_NOTIFICATIONS_ENABLED, true)
    }

    fun setNotificationsSettings(isEnabled: Boolean) {
        getSharedPref().edit().putBoolean(IS_NOTIFICATIONS_ENABLED, isEnabled).apply()
    }

    fun getLangCode(): String? {
        return getSharedPref().getString(LANG_CODE_SETTINGS_KEY, null)
    }

    fun setLangCode(langCode: String) {
        val editor = getSharedPref().edit()
        editor.putString(LANG_CODE_SETTINGS_KEY, langCode)
        editor.apply()
    }

//    fun getEpochTimeOfLastFetch(activity: Activity): Long {
//        return getSharedPref(activity).getLong(EPOCH_TIME_KEY, 0)
//    }
//
//    fun setEpochTimeOfLastFetch(activity: Activity, epochTime: Long) {
//        val editor = getSharedPref(activity).edit()
//        editor.putLong(EPOCH_TIME_KEY, epochTime)
//        editor.apply()
//    }

}