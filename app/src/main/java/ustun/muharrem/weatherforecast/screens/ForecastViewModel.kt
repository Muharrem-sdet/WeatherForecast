package ustun.muharrem.weatherforecast.screens

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        viewModelScope.launch {
            if (timePassed() or isCelsiusChanged()) forecastContainerRepository.getForecastContainer()
        }
    }

    fun initializeAppLangCode() {
        if (SharedPrefs.langCode == null) {
            SharedPrefs.langCode = when (Locale.getDefault().language) {
                "tr" -> "tr"
                else -> "en"
            }
        }
    }

    private suspend fun timePassed(): Boolean {
        var forecastEpochBefore: Long
        withContext(Dispatchers.IO) {
            forecastEpochBefore = forecastContainerRepository.dao.getForecastEpoch()
        }
        return System.currentTimeMillis() - forecastEpochBefore > THREE_HOUR_EPOCH_TIME
    }

    private suspend fun isCelsiusChanged(): Boolean {
        var isCelsiusBefore: Boolean
        withContext(Dispatchers.IO) {
            isCelsiusBefore = forecastContainerRepository.dao.getIsCelsiusFromDB()
        }
        return SharedPrefs.isCelsius != isCelsiusBefore
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