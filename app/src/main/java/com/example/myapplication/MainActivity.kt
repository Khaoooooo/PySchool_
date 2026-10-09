package com.pyschool.app.ui.splash

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import com.pyschool.app.databinding.ActivitySplashBinding
import com.pyschool.app.navigation.AppNavigator
import com.pyschool.app.ui.common.BaseActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** หน้า 01 — Splash: โชว์โลโก้ ~2 วินาทีแล้วไป Home */
class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startDotsAnimation()
        lifecycleScope.launch {
            delay(SPLASH_DURATION_MS)
            AppNavigator.finishSplash(this@SplashActivity)
        }
    }

    /** จุด 3 จุดเด้งต่อเนื่อง เหลื่อมกันทีละ 150ms */
    private fun startDotsAnimation() {
        val lift = -6f * resources.displayMetrics.density
        binding.loadingDots.children.forEachIndexed { index, dot ->
            ObjectAnimator.ofFloat(dot, "translationY", 0f, lift, 0f).apply {
                duration = 1000
                startDelay = index * 150L
                repeatCount = ObjectAnimator.INFINITE
                interpolator = AccelerateDecelerateInterpolator()
                start()
            }
        }
    }

    private companion object {
        const val SPLASH_DURATION_MS = 2_000L
    }
}

typealias MainActivity = SplashActivity
