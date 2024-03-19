package com.example.taller1_chismosos

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.taller1_chismosos.PermissionsCode.Companion.LOCATION_PERMISSION_CODE
import com.example.taller1_chismosos.databinding.ActivityBindingTryBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlin.math.roundToInt

class BindingTry : AppCompatActivity() {
    // View binding
    private lateinit var binding: ActivityBindingTryBinding
    // For getting location
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    // For getting location
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mLocationCallback: LocationCallback

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBindingTryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // For getting location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkPermissionForLocation(this)
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    private fun setUpButtons() {
        binding.buttonGetLocation.setOnClickListener {
            getLocation()
        }
    }

    private fun setUpLocationRequestAndCallback() {
        mLocationRequest = createLocationRequest()
        mLocationCallback = object : LocationCallback() {
            @SuppressLint("SetTextI18n")
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                if (location != null) {
                    binding.textViewLatitude.text = "Latitude: ${location.latitude}"
                    binding.textViewLongitude.text = "Longitude: ${location.longitude}"
                    binding.textViewHeight.text = "Height: ${location.altitude}"
                    binding.textViewDistanceFromStart.text = "Distance from start: ${distanceFromStart(location.latitude, location.longitude)} km"
                    //Get current time
                    binding.textViewTime.text = "Time: ${System.currentTimeMillis()}"
                }
            }
        }

        startLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null)
        }
    }

    private fun stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
    }

    private fun createLocationRequest() : LocationRequest =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000).apply {
            setMinUpdateIntervalMillis(5000)
        }.build()

    private fun disableButtons() {
        binding.buttonGetLocation.isEnabled = false
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        mFusedLocationClient.lastLocation.addOnSuccessListener { location ->
            Log.i("LOCATION", "Success getting location!")
            if (location != null) {
                binding.textViewLatitude.text = "Latitude: ${location.latitude}"
                binding.textViewLongitude.text = "Longitude: ${location.longitude}"
                binding.textViewHeight.text = "Height: ${location.altitude}"
                binding.textViewDistanceFromStart.text = "Distance from start: ${distanceFromStart(location.latitude, location.longitude)} km"
                //Get current time
                binding.textViewTime.text = "Time: ${System.currentTimeMillis()}"
            }
        }
    }

    private fun distanceFromStart(latitude: Double, longitude: Double) : Double {
        val startLatitude = 4.7010
        val startLongitude = -74.1461
        val earthRadius = 6371e3

        val dLat = Math.toRadians(latitude - startLatitude)
        val dLon = Math.toRadians(longitude - startLongitude)

        val a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(startLatitude)) * Math.cos(Math.toRadians(latitude)) *
                Math.sin(dLon/2) * Math.sin(dLon/2)

        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a))

        // Meters
        val result = earthRadius * c
        // Kilometers
        val resultInKm = result / 1000.0
        // Aproximate to 2 decimal places
        return (resultInKm*100.0).roundToInt() / 100.0
    }

    private fun checkPermissionForLocation(activity: AppCompatActivity) {
        when{
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                // Permission already granted
                setUpButtons()
                setUpLocationRequestAndCallback()
            }

            shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION) -> {
                // If user previously denied the permission
                Toast.makeText(this, "Permission previously denied", Toast.LENGTH_SHORT).show()
                requestPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_PERMISSION_CODE, "Needed for locating person")
            }

            else -> {
                // Always call the own function to request permission, not the system one (requestPermissions)
                requestPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_PERMISSION_CODE, "Needed for locating person")
            }

        }
    }

    private fun requestPermission(context: Activity, permission: String, requestCode: Int, justify: String) {
        if(ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            if(shouldShowRequestPermissionRationale(permission)) {
                Toast.makeText(this, justify, Toast.LENGTH_SHORT).show()
            }
            requestPermissions(arrayOf(permission), requestCode)
        } else {
            // Permission granted
            setUpButtons()
            setUpLocationRequestAndCallback()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_PERMISSION_CODE -> {
                if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission granted
                    setUpButtons()
                    setUpLocationRequestAndCallback()
                } else {
                    // Permission denied
                    disableButtons()
                }
                return
            }

            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }
}