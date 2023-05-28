package com.example.test1

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    var PERMISSIONS = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap

    lateinit var naverMapList: NaverMapItem
    lateinit var naverMapInfo: List<NaverMapData?>

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient("j3k2fo0cl0")

        setContentView(R.layout.map_fragment_activity)


        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this)

        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions,
                grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            else { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.Follow
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {

        val naverMapApiInterface =
            NaverMapRequest.getClient().create(NaverMapApiInterface::class.java)
        val call: Call<NaverMapItem> = naverMapApiInterface.getMapData()

        call.enqueue(object : Callback<NaverMapItem> {
            override fun onResponse(call: Call<NaverMapItem>, response: Response<NaverMapItem>) {
                if (response.isSuccessful) {
                    naverMapList = response.body()!!
                    naverMapInfo = naverMapList?.MAPSTOREINFO!!

                    for (i in naverMapInfo.indices) {
                        val markers = arrayOfNulls<Marker>(naverMapInfo.size)

                        markers[i] = Marker()
                        val lat = naverMapInfo[i]?.storeLat
                        val lnt = naverMapInfo[i]?.storeLng
                        markers[i]?.position = LatLng(lat!!, lnt!!)
                        markers[i]?.map = naverMap

                        val finalI = i
                        markers[i]?.setOnClickListener(Overlay.OnClickListener { overlay ->
                            Toast.makeText(application, "마커 $finalI 클릭", Toast.LENGTH_SHORT).show()
                            false
                        })
                    }

//                    val marker = Marker()
//
//                    val lat = naverMapInfo!![0]?.storeLat
//                    val lnt = naverMapInfo!![0]?.storeLng
//
//                    Log.d("TAG", "lat: $lat, lnt: $lnt")
//
//                    marker.position = LatLng(lat!!, lnt!!)
//                    marker.map = naverMap

                    // 통신 실패 처리
                }
            }

            override fun onFailure(call: Call<NaverMapItem>, t: Throwable) {
                val errorMessage = t.message
                Log.d("TAG", "Error: $errorMessage")
            }
        })


        this.naverMap = naverMap

        naverMap.setLocationSource(locationSource)
        // 권한확인. 결과는 onRequestPermissionsResult 콜백 매서드 호출


        val uiSettings = naverMap.uiSettings

        uiSettings.isLocationButtonEnabled = true

        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)
    }
}