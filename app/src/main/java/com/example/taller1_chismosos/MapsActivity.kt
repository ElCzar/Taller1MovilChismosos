package com.example.taller1_chismosos

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.taller1_chismosos.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MapStyleOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    // The map
    private var mMap: GoogleMap? = null
    private var geocoder: Geocoder? = null
    // View binding
    private lateinit var binding: ActivityMapsBinding
    // Sensors
    private lateinit var mSensorManager: SensorManager
    private lateinit var mLightSensor: Sensor
    private lateinit var mLightSensorEventListener: SensorEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Sensors
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // Light sensor
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)!!
        // Listener for light sensor
        mLightSensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: android.hardware.SensorEvent) {
                if (mMap == null) return

                val lightValue = event.values[0]
                if (lightValue > 10000) {
                    Log.i("MAPS", "LIGHT THEME\nLight value: $lightValue")
                    mMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(this@MapsActivity, R.raw.map_style_light))
                } else {
                    Log.i("MAPS", "DARK THEME\nLight value: $lightValue")
                    mMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(this@MapsActivity, R.raw.map_style_dark))
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        // Listener for options fo edit text
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                val txt = binding.etSearch.text.toString()
                if(txt.isNotEmpty() && geocoder != null) {
                    try {
                        val address = geocoder!!.getFromLocationName(txt, 2) // Can add 4 parameters to limit the search of the location

                        if(!address.isNullOrEmpty()) {
                            val location = address[0]
                            val latLng = LatLng(location.latitude, location.longitude)

                            if (mMap != null) {
                                mMap!!.moveCamera(CameraUpdateFactory.zoomTo(15F))
                                mMap!!.addMarker(MarkerOptions().position(latLng)
                                    .title("Posicion geocoder")
                                    .snippet(txt)
                                    .alpha(1F))

                                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                            }
                        } else {
                            Toast.makeText(this, "Invalid location", Toast.LENGTH_SHORT).show()
                        }

                    } catch (e: Exception) {
                        Toast.makeText(this, "Invalid location", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "No location provided", Toast.LENGTH_SHORT).show()
                }
                true
            } else {
                false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(mLightSensorEventListener, mLightSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(mLightSensorEventListener)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        geocoder = Geocoder(baseContext)

        // Enable zoom gestures
        mMap!!.uiSettings.isZoomGesturesEnabled = true
        // Change map style
        mMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_dark))

        // Add a marker in Sydney and move the camera
        val plazaBolivar = LatLng(4.5980712, -74.0761150)
        // The zoom ALWAYS comes BEFORE the camera movement to the marker
        mMap!!.moveCamera(CameraUpdateFactory.zoomTo(10F))
        // Marker with different color
        val mk1 = mMap!!.addMarker(MarkerOptions().position(plazaBolivar)
            .title("Marker in Plaza de Bolivar")
            .snippet("Poblado por palomas")
            .alpha(0.5F)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
        // Marker with custom icon
        val mk2 = mMap!!.addMarker(MarkerOptions().position(LatLng(4.5980712, -74.0711506))
            .title("Marker close Plaza de Bolivar")
            .snippet("Poblado por palomas")
            .alpha(0.5F)
            .icon(bitmapDescriptorFromVector(this, R.drawable.baseline_pets_24)))
        // Move to a location
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(plazaBolivar))

        // Add a marker if there is a long click
        mMap!!.setOnMapLongClickListener { latLng ->
            mMap!!.clear()
            mMap!!.addMarker(MarkerOptions().position(latLng)
                .title("Marker in long click")
                .snippet("Long click")
                .alpha(0.5F))
        }
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)

        vectorDrawable!!.setBounds(0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight)

        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}