package com.pyschool.app.ui.common

import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.pyschool.app.PySchoolApp

/** Activity พื้นฐานของทุกหน้า: edge-to-edge + เข้าถึง repository ได้ง่าย */
abstract class BaseActivity : AppCompatActivity() {

    protected val app: PySchoolApp get() = application as PySchoolApp

    override fun onCreate(savedInstanceState: Bundle?) {
        // ธีมแอปเป็นโทนสว่างเสมอ → บังคับไอคอน status/nav bar เป็นสีเข้ม
        val transparentLight = SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT)
        enableEdgeToEdge(statusBarStyle = transparentLight, navigationBarStyle = transparentLight)
        super.onCreate(savedInstanceState)
    }
}
