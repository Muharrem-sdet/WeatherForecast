package ustun.muharrem.weatherforecast.screens

import android.app.Activity
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ustun.muharrem.weatherforecast.data.ForecastContainer
import ustun.muharrem.weatherforecast.database.ForecastDatabase
import ustun.muharrem.weatherforecast.network.GetDataService
import ustun.muharrem.weatherforecast.repository.ForecastContainerRepository
import ustun.muharrem.weatherforecast.utilities.*

class ForecastViewModel(private val forecastContainerRepository: ForecastContainerRepository) :
    ViewModel() {
    private val _forecastListLiveData = forecastContainerRepository.forecastListLiveData
    val forecastListLiveData: LiveData<ForecastContainer>
        get() = _forecastListLiveData

    fun getForecastContainer(activity: Activity) {
        forecastContainerRepository.getForecastContainer(activity)
    }

    fun fetchForecastContainer(activity: Activity){
        forecastContainerRepository.fetchForecastContainer(activity)
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