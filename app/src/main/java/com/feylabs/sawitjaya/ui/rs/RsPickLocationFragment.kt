package com.feylabs.sawitjaya.ui.rs

import android.location.Address
import android.location.Geocoder

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.feylabs.sawitjaya.R
import com.feylabs.sawitjaya.databinding.FragmentRsPickLocationBinding
import com.feylabs.sawitjaya.ui.rs.model.GeoCoderModel
import com.feylabs.sawitjaya.ui.rs.model.RsModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*
import com.feylabs.sawitjaya.utils.base.BaseFragment

import com.google.android.gms.maps.model.Marker


class RsPickLocationFragment : BaseFragment() {

    var _binding: FragmentRsPickLocationBinding? = null
    val binding get() = _binding as FragmentRsPickLocationBinding

    var curLat: Double = 0.9434
    var curlong: Double = 116.9852
    var myMapPosition = LatLng(curLat, curlong)

    lateinit var geocoder: Geocoder

    lateinit var razMap: GoogleMap

    var addresses = mutableListOf<Address>()

    var geocodeModel = GeoCoderModel(
        "", "", "", "", ""
    )


    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        razMap = googleMap
//        val sydney = LatLng(-34.0, 151.0)
//        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        myMapPosition = LatLng(curLat, curlong)

        val marker = razMap.addMarker(
            MarkerOptions()
                .position(myMapPosition)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title("$myMapPosition")
        )

        geocoder = Geocoder(requireContext(), Locale.getDefault())

        addresses = geocoder.getFromLocation(
            marker.position.latitude,
            marker.position.longitude,
            1
        ) //1 num of possible location returned

        val cameraUpdate = CameraUpdateFactory.newLatLngZoom((myMapPosition), 7f)
        razMap.animateCamera(cameraUpdate)

        updateGeoCoder(marker, myMapPosition)

        binding.etLat.text = curLat.toString()
        binding.etLong.text = curlong.toString()

        razMap.setOnCameraMoveListener {
            val midLatLng = razMap.cameraPosition.target
            updateGeoCoder(marker, midLatLng)
        }
    }

    private fun updateGeoCoder(marker: Marker, midLatLng: LatLng) {
        if (marker != null) {
            marker.position = midLatLng
            myMapPosition = midLatLng
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rs_pick_location, container, false)
        _binding = FragmentRsPickLocationBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}