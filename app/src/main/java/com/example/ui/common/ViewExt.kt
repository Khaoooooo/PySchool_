package com.pyschool.app.ui.common

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

/** เพิ่ม padding ด้านบนเท่าความสูง status bar (ไม่ทับ padding เดิม) */
fun View.applyStatusBarPadding() {
    val base = paddingTop
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        v.updatePadding(top = base + insets.getInsets(WindowInsetsCompat.Type.systemBars()).top)
        insets
    }
}

/** เพิ่ม padding ด้านล่างเท่าความสูง navigation bar */
fun View.applyNavigationBarPadding() {
    val base = paddingBottom
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        v.updatePadding(bottom = base + insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom)
        insets
    }
}
