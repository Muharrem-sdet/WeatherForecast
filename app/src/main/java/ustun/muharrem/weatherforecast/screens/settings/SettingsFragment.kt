package ustun.muharrem.weatherforecast.screens.settings

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.layout_settings_days.*
import kotlinx.android.synthetic.main.layout_settings_item.view.*
import kotlinx.android.synthetic.main.layout_settings_notification.*
import kotlinx.android.synthetic.main.layout_settings_units.*
import ustun.muharrem.weatherforecast.R
import ustun.muharrem.weatherforecast.screens.ForecastViewModel
import ustun.muharrem.weatherforecast.screens.ForecastViewModelFactory
import ustun.muharrem.weatherforecast.utilities.SharedPrefs

class SettingsFragment : Fragment() {

    private lateinit var forecastViewModel: ForecastViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = ForecastViewModelFactory(requireActivity().application)
        forecastViewModel =
            ViewModelProvider(requireActivity(), factory).get(ForecastViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        forecastViewModel.getForecastContainer(requireActivity())
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        setClickListeners()
        setSettingsTitles()
        setSettingsSubtitles()
        setDegreeViews()
        setNotificationsCheckbox()
        setNumberOfDaysSpinner()
    }

    private fun setClickListeners() {
        activity?.let { mActivity ->
            celsius_degree_text_view.setOnClickListener {
                SharedPrefs.setIsCelsiusInSettings(mActivity, true)
                setDegreeViews()
                setUnitSubtitle()
                forecastViewModel.fetchForecastContainer(mActivity)
            }
            fahrenheit_degree_text_view.setOnClickListener {
                SharedPrefs.setIsCelsiusInSettings(mActivity, false)
                setDegreeViews()
                setUnitSubtitle()
                forecastViewModel.fetchForecastContainer(mActivity)
            }

            days_settings_spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    val numberOfDays: String = parent?.getItemAtPosition(position).toString()
                    SharedPrefs.setNumberOfDays(mActivity, numberOfDays)
                    setNumberOfDaysSubtitle()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            notification_settings_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                SharedPrefs.setNotificationsSettings(mActivity, isChecked)
                setNotificationsSubtitle()
                val toastMessage =
                    if (isChecked) getString(R.string.weather_notifications_enabled) else getString(
                        R.string.weather_notifications_disabled
                    )
                Toast.makeText(mActivity, toastMessage, Toast.LENGTH_SHORT).show()
                //  TODO other checkbox listeners actions to send notifications!!!
            }
        }
    }

    private fun setNotificationsCheckbox() {
        activity?.let {
            notification_settings_checkbox.isChecked = SharedPrefs.getNotificationsSettings(it)
        }
    }

    private fun setDegreeViews() {
        activity?.let {
            val isCelsius = SharedPrefs.getIsCelsiusFromSettings(it)
            if (isCelsius) {
                celsius_degree_text_view.setTextColor(Color.BLACK)
                fahrenheit_degree_text_view.setTextColor(Color.GRAY)
                celsius_degree_text_view.setTypeface(Typeface.DEFAULT_BOLD)
                fahrenheit_degree_text_view.setTypeface(Typeface.DEFAULT)
            } else {
                celsius_degree_text_view.setTextColor(Color.GRAY)
                fahrenheit_degree_text_view.setTextColor(Color.BLACK)
                celsius_degree_text_view.setTypeface(Typeface.DEFAULT)
                fahrenheit_degree_text_view.setTypeface(Typeface.DEFAULT_BOLD)
            }
        }
    }

    private fun setSettingsSubtitles() {
        setUnitSubtitle()
        setNumberOfDaysSubtitle()
        setNotificationsSubtitle()
    }

    private fun setUnitSubtitle() {
        activity?.let {
            val isCelsius = SharedPrefs.getIsCelsiusFromSettings(it)
            units_settings_item.settings_value.text = if (isCelsius) {
                getString(R.string.celsius)
            } else {
                getString(R.string.fahrenheit)
            }
        }
    }

    private fun setNotificationsSubtitle() {
        activity?.let {
            val isEnabled = SharedPrefs.getNotificationsSettings(it)
            notification_settings_item.settings_value.text = if (isEnabled) {
                getString(R.string.enabled)
            } else {
                getString(R.string.disabled)
            }
        }
    }

    private fun setNumberOfDaysSubtitle() {
        activity?.let {
            days_settings_item.settings_value.text = SharedPrefs.getNumberOfDays(it).plus(
                getString(R.string.day_forecast)
            )
        }
    }

    private fun setSettingsTitles() {
        notification_settings_item.settings_name.text =
            getString(R.string.weather_notification_settings_label)
        units_settings_item.settings_name.text = getString(R.string.units_settings_label)
        days_settings_item.settings_name.text = getString(R.string.number_days_settings_label)
    }

    private fun setNumberOfDaysSpinner() {
        activity?.let {
            val adapter = ArrayAdapter.createFromResource(
                it, R.array.number_of_days, android.R.layout.simple_spinner_item
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            days_settings_spinner.adapter = adapter
            // Selects the default number of days value for the first time
            val numberOfDays = SharedPrefs.getNumberOfDays(it)
            val position: Int = adapter.getPosition(numberOfDays)
            days_settings_spinner.setSelection(position)
        }
    }
}