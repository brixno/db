package com.example.test1

import com.google.gson.annotations.SerializedName

data class NaverMapData(
    @SerializedName("id")
    val serialNum: Int,
    @SerializedName("자전거대여소명") // php 파일과 이름이 같아야 함.
    val storeName: String,
    @SerializedName("자전거대여소구분")
    val storeCategory: String,
    @SerializedName("소재지지번주소")
    val storeLocation: String,
    @SerializedName("위도")
    val storeLat: Double,
    @SerializedName("경도")
    val storeLng: Double,
    @SerializedName("자전거보유대수")
    val storeCnt: Int,
    @SerializedName("관리기관전화번호")
    val managerNum: String,
)