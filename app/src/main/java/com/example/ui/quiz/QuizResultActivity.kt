package com.pyschool.app.ui.quiz

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.pyschool.app.R
import com.pyschool.app.databinding.ActivityQuizResultBinding
import com.pyschool.app.navigation.AppNavigator
import com.pyschool.app.navigation.Screen
import com.pyschool.app.ui.common.BaseActivity
import com.pyschool.app.ui.common.applyNavigationBarPadding
import com.pyschool.app.ui.common.applyStatusBarPadding

/**
 * หน้า 09 — Quiz Result
 * รับคะแนนจาก Intent (QuizActivity บันทึกลง SQLite ไปแล้วก่อนเปิดหน้านี้)
 * ปุ่ม: Try Again / View History / Back to Home — กด Back ของระบบ = กลับ Home
 */
class QuizResultActivity : BaseActivity() {

    private lateinit var binding: ActivityQuizResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.content.applyStatusBarPadding()
        binding.content.applyNavigationBarPadding()

        val score = intent.getIntExtra(AppNavigator.EXTRA_SCORE, 0)
        val total = intent.getIntExtra(AppNavigator.EXTRA_TOTAL, 10).coerceAtLeast(1)
        val percent = score * 100 / total

        binding.scoreText.text = getString(R.string.score_format, score, total)
        binding.percentText.text = getString(R.string.percent_format, percent)
        binding.scoreRing.animateTo(percent / 100f)

        val (title, text) = messageFor(percent)
        binding.messageTitle.setText(title)
        binding.messageText.setText(text)

        binding.btnTryAgain.setOnClickListener { AppNavigator.open(this, Screen.QUIZ, finishCurrent = true) }
        binding.btnHistory.setOnClickListener { AppNavigator.open(this, Screen.HISTORY, finishCurrent = true) }
        binding.btnHome.setOnClickListener { AppNavigator.goHome(this) }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() = AppNavigator.goHome(this@QuizResultActivity)
        })
    }

    private fun messageFor(percent: Int): Pair<Int, Int> = when {
        percent >= 80 -> R.string.result_msg_great_title to R.string.result_msg_great_text
        percent >= 50 -> R.string.result_msg_good_title to R.string.result_msg_good_text
        else -> R.string.result_msg_practice_title to R.string.result_msg_practice_text
    }
}
