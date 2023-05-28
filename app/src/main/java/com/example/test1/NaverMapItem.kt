package com.example.test1

import com.example.test1.NaverMapData
import com.google.gson.annotations.SerializedName

class NaverMapItem {
    @SerializedName("MAPSTOREINFO")
    var MAPSTOREINFO: List<NaverMapData>? = null
}