package ustun.muharrem.weatherforecast.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ustun.muharrem.weatherforecast.data.ForecastContainer

@Dao
interface ForecastContainerDao {
    @Query("SELECT * FROM forecastContainers")
    fun getForecastContainer(): LiveData<ForecastContainer>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(forecastContainer: ForecastContainer)

    @Query("DELETE FROM forecastContainers")
    fun deleteAll()

    @Query("SELECT  forecastEpoch from forecastContainers")
    fun getForecastEpoch() : Long

    @Query("UPDATE forecastContainers SET forecastEpoch = :currentEpoch")
    fun updateForecastEpoch(currentEpoch: Long)

    @Query("SELECT  isCelsius from forecastContainers")
    fun getIsCelsiusFromDB() : Boolean

    @Query("UPDATE forecastContainers SET isCelsius = :currentValue")
    fun updateIsCelsius(currentValue: Boolean)
}