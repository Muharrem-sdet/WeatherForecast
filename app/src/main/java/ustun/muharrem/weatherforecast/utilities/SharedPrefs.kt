package ustun.muharrem.weatherforecast.utilities

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

object SharedPrefs {

    var application = Application()
    private val sharedPref: SharedPreferences
        get() = application.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    var isCelsius: Boolean
        get() = sharedPref.getBoolean(IS_CELSIUS_SETTING_KEY, IS_CELSIUS_DEFAULT_VALUE)
        set(value) = sharedPref.edit().putBoolean(IS_CELSIUS_SETTING_KEY, value).apply()

    var numberOfDays: String?
        get() = sharedPref.getString(NUMBER_OF_DAYS_SETTING_KEY, "16")
        set(value) = sharedPref.edit().putString(NUMBER_OF_DAYS_SETTING_KEY, value).apply()

    var notificationEnabled: Boolean
        get() = sharedPref.getBoolean(IS_NOTIFICATIONS_ENABLED, true)
        set(value) = sharedPref.edit().putBoolean(IS_NOTIFICATIONS_ENABLED, value).apply()

    var langCode: String?
        get() = sharedPref.getString(LANG_CODE_SETTINGS_KEY, null)
        set(value) = sharedPref.edit().putString(LANG_CODE_SETTINGS_KEY, value).apply()


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