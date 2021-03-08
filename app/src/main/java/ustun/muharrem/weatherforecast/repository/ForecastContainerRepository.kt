package ustun.muharrem.weatherforecast.repository

import android.app.Activity
import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ustun.muharrem.weatherforecast.data.ForecastContainer
import ustun.muharrem.weatherforecast.database.ForecastContainerDao
import ustun.muharrem.weatherforecast.network.GetDataService
import ustun.muharrem.weatherforecast.utilities.*

class ForecastContainerRepository(private val dao: ForecastContainerDao) {

    val forecastListLiveData: LiveData<ForecastContainer> = dao.getForecastContainer()
//    val forecastEpochNow = System.currentTimeMillis()
//    val forecastEpochBefore = dao.getForecastEpoch()

    fun getForecastContainer(activity: Activity) {
        fetchForecastContainer(activity)
    }

    private fun insertToDatabase(forecastContainer: ForecastContainer) {
        Thread {
            // No need to delete because it replaces in case of a conflict
            dao.insert(forecastContainer)
        }.start()
    }

    fun fetchForecastContainer(activity: Activity) {
        activity.let {
            val isCelsius = SharedPrefs.getIsCelsiusFromSettings(it)
            val days = SharedPrefs.getNumberOfDays(it)
            val langCode = SharedPrefs.getLangCode(it)
            val units = if (isCelsius) METRIC_QUERY_PARAM_VALUE else IMPERIAL_QUERY_PARAM_VALUE
            val getDataService = RetrofitClient.retrofit?.create(GetDataService::class.java)
            val callForecast =
                getDataService?.getForecast(langCode!!, days!!, units, 43026, API_KEY)
            callForecast?.enqueue(object : Callback<ForecastContainer> {
                override fun onResponse(
                    call: Call<ForecastContainer>,
                    response: Response<ForecastContainer>
                ) {
                    val forecastContainer: ForecastContainer? = response.body()
                    forecastContainer?.let {
                        insertToDatabase(it)
//                        val currentEpoch = System.currentTimeMillis()
//                        dao.setCurrentForecastEpoch(currentEpoch)
                    }
                }

                override fun onFailure(call: Call<ForecastContainer>, t: Throwable) {
                    // TODO add some error messages for the user
                    t.stackTrace
                }
            })
        }
    }
}