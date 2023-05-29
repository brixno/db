package com.example.test1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.gson.annotations.SerializedName
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.NaverMapSdk
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

        setContentView(R.layout.activity_main)


        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
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

                        val btnToggleBicycle = findViewById<Button>(R.id.btn_toggle_bicycle)
                        btnToggleBicycle.setOnClickListener {
                            val isBicycleLayerEnabled = naverMap.isLayerGroupEnabled(NaverMap.LAYER_GROUP_BICYCLE)
                            naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BICYCLE, !isBicycleLayerEnabled)
                            if (!isBicycleLayerEnabled) {
                                btnToggleBicycle.text = "자전거 도로"
                            } else {
                                btnToggleBicycle.text = "자전거 도로"
                            }
                        }

                        val finalI = i
                        markers[i]?.setOnClickListener(object : Overlay.OnClickListener {
                            override fun onClick(@NonNull overlay: Overlay): Boolean {

                                val mapInfoName = naverMapInfo[finalI]?.storeName
                                val mapInfoLoca = naverMapInfo[finalI]?.storeLocation
                                val mapInfoCate = naverMapInfo[finalI]?.storeCategory
                                val mapInfoCnt = naverMapInfo[finalI]?.storeCnt
                                val mapInfoNum = naverMapInfo[finalI]?.managerNum

                                val getMapInfoName = findViewById<TextView>(R.id.map_info_name)
                                val getMapInfoLoca = findViewById<TextView>(R.id.map_info_addr)
                                val getmapInfoCate = findViewById<TextView>(R.id.map_info_cate)
                                val getmapInfoCnt = findViewById<TextView>(R.id.map_info_cnt)
                                val getmapInfoNum = findViewById<TextView>(R.id.map_info_num)



                                getMapInfoName?.text = mapInfoName
                                getMapInfoLoca?.text = mapInfoLoca
                                getmapInfoCate?.text = mapInfoCate
                                getmapInfoCnt?.text = "자전거보유대수: " + mapInfoCnt.toString()
                                getmapInfoNum?.text = "전화번호: " + mapInfoNum

                                var mapInfoLayout = findViewById<LinearLayout>(R.id.map_info_layout2)

                                mapInfoLayout?.visibility = View.VISIBLE

                                return false
                            }
                        })
                    }
                }
            }
            override fun onFailure(call: Call<NaverMapItem>, t: Throwable) { // 통신 실패 처리
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