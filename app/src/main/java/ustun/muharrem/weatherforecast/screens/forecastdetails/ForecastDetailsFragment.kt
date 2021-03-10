package ustun.muharrem.weatherforecast.screens.forecastdetails

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_forecast_details.*
import kotlinx.android.synthetic.main.layout_forecast_details_item.view.*
import ustun.muharrem.weatherforecast.R
import ustun.muharrem.weatherforecast.data.Forecast
import ustun.muharrem.weatherforecast.screens.ForecastViewModel
import ustun.muharrem.weatherforecast.screens.ForecastViewModelFactory
import ustun.muharrem.weatherforecast.utilities.DateUtil
import ustun.muharrem.weatherforecast.utilities.DrawableManager.getImageId
import ustun.muharrem.weatherforecast.utilities.SharedPrefs
import java.util.*
import kotlin.math.roundToInt

class ForecastDetailsFragment : Fragment() {
    private val args: ForecastDetailsFragmentArgs by navArgs()
    private lateinit var forecastViewModel: ForecastViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = ForecastViewModelFactory(requireActivity().application)
        forecastViewModel =
            ViewModelProvider(requireActivity(), factory).get(ForecastViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        forecastViewModel.getForecastContainer()
        return inflater.inflate(R.layout.fragment_forecast_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDetailsItemLabels()

        forecastViewModel.forecastListLiveData.observe(viewLifecycleOwner, Observer {
            val position = Math.min(args.position, it.data.size-1)
            city_name.text = it.city_name
            setDetailsHeaderValues(it.data[position])
            setDetailsItemValues(it.data[position])
        })
    }

    private fun setDetailsHeaderValues(forecast: Forecast) {
        date_text_view.text = DateUtil.getDateText(forecast.valid_date, 0)
        icon_image_view.setImageResource(R.drawable::class.java.getImageId(forecast.weather.icon))
        high_temp_text_view.text = forecast.high_temp.roundToInt().toString().plus("°")
        low_temp_text_view.text = forecast.low_temp.roundToInt().toString().plus("°")
        weather_description_text_view.text = forecast.weather.description
    }

    private fun setDetailsItemValues(forecast: Forecast) {
            sunrise_time_details_item.forecast_details_value.text =
                DateUtil.getTimeText(forecast.sunrise_ts)
            sunset_time_details_item.forecast_details_value.text =
                DateUtil.getTimeText(forecast.sunset_ts)
            val windSpeedUnit = if (SharedPrefs.getIsCelsiusFromSettings()) " m/s" else " mph"
            wind_speed_details_item.forecast_details_value.text =
                forecast.wind_spd.toString().plus(windSpeedUnit)
            wind_direction_details_item.forecast_details_value.text =
                forecast.wind_cdir_full.capitalize(Locale.ROOT)
            humidity_details_item.forecast_details_value.text = forecast.rh.toString().plus("%")
            precipitation_probability_details_item.forecast_details_value.text =
                forecast.pop.toString().plus("%")
            val precipUnit = if (SharedPrefs.getIsCelsiusFromSettings()) " mm" else " in"
            liquid_precipitation_details_item.forecast_details_value.text =
                forecast.precip.toString().plus(precipUnit)
            pressure_details_item.forecast_details_value.text =
                forecast.pres.toString().plus(" mbar")
            val visLength = if (SharedPrefs.getIsCelsiusFromSettings()) " km" else " mi"
            visibility_details_item.forecast_details_value.text =
                forecast.vis.toString().plus(visLength)
            max_uv_index_details_item.forecast_details_value.text = forecast.uv.toString()
    }

    private fun setDetailsItemLabels() {
        sunrise_time_details_item.forecast_details_label.text = getString(R.string.sunrise_label)
        sunset_time_details_item.forecast_details_label.text = getString(R.string.sunset_label)
        wind_speed_details_item.forecast_details_label.text = getString(R.string.wind_speed_label)
        wind_direction_details_item.forecast_details_label.text =
            getString(R.string.wind_direction_label)
        humidity_details_item.forecast_details_label.text = getString(R.string.humidity_label)
        precipitation_probability_details_item.forecast_details_label.text =
            getString(R.string.precipitation_probability_label)
        liquid_precipitation_details_item.forecast_details_label.text =
            getString(R.string.accumulated_precipitation_label)
        pressure_details_item.forecast_details_label.text =
            getString(R.string.average_pressure_label)
        visibility_details_item.forecast_details_label.text = getString(R.string.visibility_label)
        max_uv_index_details_item.forecast_details_label.text =
            getString(R.string.max_uv_index_label)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_details_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                val direction =
                    ForecastDetailsFragmentDirections.actionForecastDetailsFragmentToSettingsFragment()
                findNavController().navigate(direction)
            }
            R.id.share -> {
                // TODO: Share the forecast details as a text
            }
        }
        return super.onOptionsItemSelected(item)
    }
}