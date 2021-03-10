package ustun.muharrem.weatherforecast.repository

import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ustun.muharrem.weatherforecast.data.ForecastContainer
import ustun.muharrem.weatherforecast.database.ForecastContainerDao
import ustun.muharrem.weatherforecast.network.GetDataService
import ustun.muharrem.weatherforecast.utilities.*

class ForecastContainerRepository(val dao: ForecastContainerDao) {

    val forecastListLiveData: LiveData<ForecastContainer> = dao.getForecastContainer()

    suspend fun getForecastContainer() {
        withContext(Dispatchers.IO) {
//            if (timePassed() or isCelsiusChanged())
                fetchForecastContainer()
        }
    }

//    private suspend fun timePassed(): Boolean {
//        var forecastEpochBefore: Long
//        withContext(Dispatchers.IO) {
//            forecastEpochBefore = dao.getForecastEpoch()
//        }
//        return System.currentTimeMillis() - forecastEpochBefore > THREE_HOUR_EPOCH_TIME
//    }
//
//    private suspend fun isCelsiusChanged(): Boolean{
//        var isCelsiusBefore: Boolean
//        withContext(Dispatchers.IO){
//            isCelsiusBefore = dao.getIsCelsiusFromDB()
//        }
//        return SharedPrefs.isCelsius != isCelsiusBefore
//    }

    private fun insertToDatabase(forecastContainer: ForecastContainer) {
        Thread {
            dao.insert(forecastContainer)
            dao.updateForecastEpoch(System.currentTimeMillis())
            dao.updateIsCelsius(SharedPrefs.isCelsius)
        }.start()
    }

    private fun fetchForecastContainer() {
        val langCode = SharedPrefs.langCode
        val units =
            if (SharedPrefs.isCelsius) METRIC_QUERY_PARAM_VALUE else IMPERIAL_QUERY_PARAM_VALUE
        val getDataService = RetrofitClient.retrofit?.create(GetDataService::class.java)
        val callForecast =
            getDataService?.getForecast(langCode!!, units, 43026, API_KEY)

        callForecast?.enqueue(object : Callback<ForecastContainer> {
            override fun onResponse(
                call: Call<ForecastContainer>,
                response: Response<ForecastContainer>
            ) {
                val forecastContainer: ForecastContainer? = response.body()
                forecastContainer?.let {
                    Log.d("MyApp", "ForecastContainer is NOT null")
                    insertToDatabase(it)
                }
            }

            override fun onFailure(call: Call<ForecastContainer>, t: Throwable) {
                // TODO add some error messages for the user
                t.localizedMessage?.let { Log.d("MyApp", it) }
                Log.d("MyApp", "I am onFailureCall")
            }
        })

    }
}