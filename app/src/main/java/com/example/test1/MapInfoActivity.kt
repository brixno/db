package com.example.test1

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Display
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.lang.Math.round
import kotlin.math.roundToInt


class MapInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
    }

    override fun onResume() {
        super.onResume()

        var getMapInfoName : TextView? = findViewById(R.id.map_info_name)
        var getMapInfoLoca : TextView? = findViewById(R.id.map_info_addr)

        val intent = intent
        val name : String? = intent.getStringExtra("name")
        val loca : String? = intent.getStringExtra("loca")

        getMapInfoName?.text = name
        getMapInfoLoca?.text = loca

        initLayout()
    }

    private fun initLayout() {

        val width : Int
        val height : Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // API Level 30 버전
            val windowMetrics = windowManager.currentWindowMetrics
            width = windowMetrics.bounds.width()
            height = windowMetrics.bounds.height()
        } else { // API Level 30 이전 버전
            val display: Display = windowManager.defaultDisplay
            val displayMetrics = DisplayMetrics()
            display.getRealMetrics(displayMetrics)
            width = displayMetrics.widthPixels
            height = displayMetrics.heightPixels
        }
        window.setLayout((width * 0.9).roundToInt() as Int, (height * 0.22).roundToInt() as Int)
        window.setGravity(Gravity.BOTTOM)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}