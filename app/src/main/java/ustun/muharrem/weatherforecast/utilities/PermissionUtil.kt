package ustun.muharrem.weatherforecast.utilities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat

object PermissionUtil {

    fun permissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun showPermissionDialog(activity: AppCompatActivity) {

        // TODO Add show rationale in the future
        //  if(shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)){}

    }
}