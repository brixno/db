package com.example.test1
import retrofit2.Call
import retrofit2.http.GET

interface NaverMapApiInterface {
    @GET("bicycle_rental.php")
    fun getMapData(): Call<NaverMapItem>
}