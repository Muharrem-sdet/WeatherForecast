package ustun.muharrem.weatherforecast.screens

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ustun.muharrem.weatherforecast.data.ForecastContainer
import ustun.muharrem.weatherforecast.database.ForecastDatabase
import ustun.muharrem.weatherforecast.repository.ForecastContainerRepository
import ustun.muharrem.weatherforecast.utilities.*
import java.util.*

class ForecastViewModel(private val forecastContainerRepository: ForecastContainerRepository) :
    ViewModel() {
    private val _forecastListLiveData = forecastContainerRepository.forecastListLiveData
    val forecastListLiveData: LiveData<ForecastContainer>
        get() = _forecastListLiveData

    fun getForecastContainer() {
            forecastContainerRepository.getForecastContainer()
    }

        fun initializeAppLangCode() {
        if (SharedPrefs.getLangCode() == null) {
            var langCode = Locale.getDefault().language
            langCode = when (langCode) {
                "tr" -> "tr"
                else -> "en"
            }
            SharedPrefs.setLangCode(langCode)
        }
    }
}

class ForecastViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForecastViewModel::class.java)) {
            val dao = ForecastDatabase.getDatabase(application).getForecastContainerDao()
            val repository = ForecastContainerRepository(dao)
            @Suppress("UNCHECKED_CAST")
            return ForecastViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}