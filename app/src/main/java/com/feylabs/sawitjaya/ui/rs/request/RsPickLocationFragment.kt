package com.feylabs.sawitjaya.ui.rs.request

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.LocationListener
import android.location.LocationManager

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.databinding.FragmentRsPickLocationBinding
import com.feylabs.sawitjaya.ui.rs.request.model.GeoCoderModel
import com.feylabs.sawitjaya.ui.rs.request.model.RsModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*
import com.feylabs.sawitjaya.ui.base.BaseFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.model.Marker


class RsPickLocationFragment : BaseFragment() {
    var marker: Marker? = null

    var _binding: FragmentRsPickLocationBinding? = null
    val binding get() = _binding as FragmentRsPickLocationBinding

    private lateinit var myMap: GoogleMap

    lateinit var locationManager: LocationManager
    lateinit var locationListener: LocationListener
    lateinit var userLatLng: LatLng

    var curLat: Double = 0.9434
    var curlong: Double = 116.9852

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    var addresses = mutableListOf<Address>()

    lateinit var geocoder: Geocoder
    var geocodeModel = GeoCoderModel(
        "", "", "", "", ""
    )


    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->

        myMap = googleMap
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        geocoder = Geocoder(requireContext(), Locale.getDefault())
        userLatLng = LatLng(0.0, 0.0)

        myMap.isMyLocationEnabled = true
        myMap.uiSettings.isMyLocationButtonEnabled = true

        getLastKnownLocation()

        myMap.setOnCameraMoveListener {
            val midLatLng = myMap.cameraPosition.target
            marker?.let { updateGeoCoder(it, midLatLng) }
        }

        myMap.setOnMapClickListener { latlng -> // TODO Auto-generated method stub
            if (marker != null) {
                marker?.remove()
            }
            userLatLng = latlng
            marker = myMap.addMarker(
                MarkerOptions()
                    .position(latlng)
                    .icon(
                        BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED)
                    )
            )
            println(latlng)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRsPickLocationBinding.inflate(inflater)
        return binding.root
    }

    override fun initUI() {

    }

    override fun initObserver() {
    }

    override fun initAction() {
    }

    override fun initData() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

//        val autocompleteFragment =
//            requireActivity().supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
//                    as AutocompleteSupportFragment
//        // Specify the types of place data to return.
//        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

        // Set up a PlaceSelectionListener to handle the response.
//        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
//            override fun onPlaceSelected(place: Place) {
//                // TODO: Get info about the selected place.
//                Log.i(TAG, "Place: ${place.name}, ${place.id}")
//            }
//
//            override fun onError(status: Status) {
//                // TODO: Handle the error.
//                Log.i(TAG, "An error occurred: $status")
//            }
//        })


    }

    @SuppressLint("MissingPermission")
    //Permission Already Checked at Parent Activity
    private fun getLastKnownLocation() {

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    myMap.clear()
                    userLatLng = LatLng(location.latitude, location.longitude)
                    val myLocation = userLatLng
                    marker = myMap.addMarker(
                        MarkerOptions()
                            .position(myLocation)
                            .icon(
                                BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_RED)
                            )
                    )
                    myMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
                    myMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f))
                }
            }
    }

    private fun updateGeoCoder(marker: Marker, midLatLng: LatLng) {
        if (marker != null) {
            marker.position = midLatLng
            userLatLng = midLatLng
            //updating current latitude and longitude
            curLat = marker.position.latitude
            curlong = marker.position.longitude
            geocoder = Geocoder(requireContext(), Locale.getDefault())
            addresses = geocoder.getFromLocation(
                marker.position.latitude,
                marker.position.longitude,
                1
            ) //1 num of possible location returned

            binding.btnNextDetailTBS.setOnClickListener {
                findNavController().navigate(
                    R.id.action_rsPickLocationFragment_to_rsDetailFragment,
                    bundleOf(
                        "rs" to RsModel(
                            address = binding.mapsAddress.text.toString(),
                            lat = binding.etLat.text.toString(),
                            long = binding.etLong.text.toString()
                        )
                    )
                )
            }

            binding.mapsAddress.setText("Memuat Alamat Anda")
            binding.etLat.text = curLat.toString()
            binding.etLong.text = curlong.toString()
            Handler().postDelayed({

                binding.apply {
                    //0 to obtain first possible address
                    var address: String? = addresses[0].getAddressLine(0)
                    var city: String? = addresses[0].locality ?: ""
                    var state: String? = addresses[0].adminArea ?: ""
                    var country: String? = addresses[0].countryName ?: ""
                    var postalCode: String? = addresses[0].postalCode ?: ""


                    val titleMarker = "$address-$city-$state"
                    address = addresses[0].getAddressLine(0) ?: ""
                    binding.mapsAddress.setText(titleMarker)
                    marker.title = titleMarker
                    binding.etLat.text = curLat.toString()
                    binding.etLong.text = curlong.toString()
                }

            }, 2500)
        }
    }
}