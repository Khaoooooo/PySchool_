package com.pyschool.app.ui.history

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.pyschool.app.R
import com.pyschool.app.data.model.HistorySummary
import com.pyschool.app.data.model.QuizAttempt
import com.pyschool.app.databinding.ActivityHistoryBinding
import com.pyschool.app.databinding.ItemHistoryAttemptBinding
import com.pyschool.app.navigation.AppNavigator
import com.pyschool.app.navigation.Screen
import com.pyschool.app.ui.common.BaseActivity
import com.pyschool.app.ui.common.applyStatusBarPadding
import com.pyschool.app.ui.common.highlightedTitle
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * หน้า 10 — History
 * อ่านจาก SQLite: คะแนนสูงสุด (Best Score), จำนวนครั้งที่ทำ และรายการล่าสุด
 */
class HistoryActivity : BaseActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.root.applyStatusBarPadding()
        binding.header.headerTitle.setText(R.string.nav_history)
        binding.header.backButton.setOnClickListener { finish() }
        binding.bottomNav.setActive(Screen.HISTORY)

        val word = getString(R.string.history_title_word)
        binding.historyTitle.text = highlightedTitle(this, getString(R.string.history_title, word), word)
        binding.btnMoreQuiz.setOnClickListener { AppNavigator.openTab(this, Screen.QUIZ) }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val summary = app.quizRepository.getSummary()
            val attempts = app.quizRepository.getRecentAttempts()
            render(summary, attempts)
        }
    }

    private fun render(summary: HistorySummary, attempts: List<QuizAttempt>) {
        val hasData = summary.attemptCount > 0
        binding.bestScore.text =
            if (hasData) getString(R.string.percent_format, summary.bestPercent) else getString(R.string.dash)
        binding.quizzesTaken.text = summary.attemptCount.toString()

        binding.emptyCard.visibility = if (hasData) View.GONE else View.VISIBLE
        binding.attemptList.removeAllViews()
        // ป้าย BEST แสดงที่ครั้งที่ได้คะแนนสูงสุด (ถ้าเท่ากันเลือกครั้งล่าสุด)
        val bestId = attempts.maxByOrNull { it.percent }?.id
        attempts.forEach { binding.attemptList.addView(createAttemptCard(it, it.id == bestId)) }
    }

    private fun createAttemptCard(attempt: QuizAttempt, isBest: Boolean): View {
        val item = ItemHistoryAttemptBinding.inflate(layoutInflater, binding.attemptList, false)
        item.attemptDate.text = dateFormat.format(Date(attempt.takenAt))
        item.attemptProgress.progress = attempt.percent
        item.attemptScore.text = getString(R.string.score_format, attempt.score, attempt.total)
        item.attemptTag.text =
            if (isBest) getString(R.string.history_best_tag) else getString(R.string.percent_format, attempt.percent)
        return item.root
    }
}
