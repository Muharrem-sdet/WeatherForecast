package ustun.muharrem.weatherforecast.utilities

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

//class SharedPrefs {
object SharedPrefs {

/*    companion object {
        var application = Application()

        @Volatile
        private var INSTANCE: SharedPreferences? = null
        fun getInstance(): SharedPreferences {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = application.getSharedPreferences(
                        SHARED_PREFERENCES_NAME,
                        Context.MODE_PRIVATE
                    )
                    INSTANCE = instance
                }
                return instance!!
            }
        }
    }*/

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
}