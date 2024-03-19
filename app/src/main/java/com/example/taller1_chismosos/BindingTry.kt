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
import com.google.android.gms.location.LocationServices

class BindingTry : AppCompatActivity() {
    // View binding
    private lateinit var binding: ActivityBindingTryBinding
    // For getting location
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBindingTryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // For getting location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkPermissionForLocation(this)

    }

    private fun setUpButtons() {
        binding.buttonGetLocation.setOnClickListener {
            getLocation()
        }
    }

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
            }
        }
    }

    private fun checkPermissionForLocation(activity: AppCompatActivity) {
        when{
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                // Permission already granted
                setUpButtons()
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
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_PERMISSION_CODE -> {
                if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission granted
                    setUpButtons()
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