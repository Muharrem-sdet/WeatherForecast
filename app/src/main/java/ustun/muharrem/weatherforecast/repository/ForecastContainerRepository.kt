package ustun.muharrem.weatherforecast.repository

import android.app.Activity
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

class ForecastContainerRepository(private val dao: ForecastContainerDao) {

    val forecastListLiveData: LiveData<ForecastContainer> = dao.getForecastContainer()

    fun getForecastContainer() {
//        withContext(Dispatchers.IO) {
//            val forecastEpochNow = System.currentTimeMillis()
//            val forecastEpochBefore = dao.getForecastEpoch()
//            val timePassed = forecastEpochNow - forecastEpochBefore > THREE_HOUR_EPOCH_TIME
//            if(timePassed)
        fetchForecastContainer()
//        }
//        }
    }

    private fun insertToDatabase(forecastContainer: ForecastContainer) {
        Log.d("MyApp", "I am in the fun of insert it to database")

        Thread {
            Log.d("MyApp", "I am about to insert it to database")

            dao.insert(forecastContainer)
/*           val currentEpoch = System.currentTimeMillis()
//            dao.updateCurrentForecastEpoch(currentEpoch)
*/
        }.start()
        Log.d("MyApp", "I am at the end of the fun of insert it to database")


    }

    private fun fetchForecastContainer() {
            val isCelsius = SharedPrefs.getIsCelsiusFromSettings()
            val days = SharedPrefs.getNumberOfDays()
            val langCode = SharedPrefs.getLangCode()
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