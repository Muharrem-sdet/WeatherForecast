package ustun.muharrem.weatherforecast.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ustun.muharrem.weatherforecast.data.ForecastContainer
import ustun.muharrem.weatherforecast.data.ForecastContainerResult
import ustun.muharrem.weatherforecast.database.ForecastContainerDao
import ustun.muharrem.weatherforecast.network.GetDataService
import ustun.muharrem.weatherforecast.utilities.*
import java.lang.Error
import java.lang.Exception

class ForecastContainerRepository(private val dao: ForecastContainerDao) {

    val forecastContainerResultLiveData = MutableLiveData<ForecastContainerResult>()

    suspend fun getPreviouslySavedForecastContainer() {
        withContext(Dispatchers.IO) {
            //What happens if the app runs for the very first time???
            val forecastContainer = dao.getForecastContainer()
//            forecastContainer?.let{
            forecastContainerResultLiveData.postValue(
                ForecastContainerResult.Success(forecastContainer)
            )
//            }
        }
    }

    suspend fun timePassed(): Boolean {
        var forecastEpochBefore: Long
        withContext(Dispatchers.IO) {
            forecastEpochBefore = dao.getForecastEpoch()
        }
        return System.currentTimeMillis() - forecastEpochBefore > THREE_HOUR_EPOCH_TIME
    }

    suspend fun isCelsiusChanged(): Boolean {
        var isCelsiusBefore: Boolean
        withContext(Dispatchers.IO) {
            isCelsiusBefore = dao.getIsCelsiusFromDB()
        }
        return SharedPrefs.isCelsius != isCelsiusBefore
    }

    private fun insertToDatabase(forecastContainer: ForecastContainer) {
        dao.insert(forecastContainer)
        dao.updateForecastEpoch(System.currentTimeMillis())
        dao.updateIsCelsius(SharedPrefs.isCelsius)
    }

    suspend fun fetchForecastContainer() {
        withContext(Dispatchers.IO) {
            val getDataService = RetrofitClient.retrofit?.create(GetDataService::class.java)
            val langCode = SharedPrefs.langCode
            val units =
                if (SharedPrefs.isCelsius) METRIC_QUERY_PARAM_VALUE else IMPERIAL_QUERY_PARAM_VALUE
            val callForecast =
                getDataService?.getForecast(langCode!!, units, 43026, API_KEY)

            try {
                val response = callForecast?.execute()
                val forecastContainer = response?.body()
                forecastContainer?.let {
                    forecastContainerResultLiveData.postValue(ForecastContainerResult.Success(it))
                    insertToDatabase(it)
                }
                // TODO: Handle if forecastContainer is null
            } catch (ex: Exception) {
                ex.localizedMessage?.let { Log.d("MyApp", it) }
                ForecastContainerResult.Failure(Error(ex.message))

            }
        }
    }
}