package com.pyschool.app.ui.quiz

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.lifecycle.lifecycleScope
import com.pyschool.app.R
import com.pyschool.app.databinding.ActivityQuizBinding
import com.pyschool.app.databinding.BlockCodeBinding
import com.pyschool.app.databinding.ItemQuizOptionBinding
import com.pyschool.app.navigation.AppNavigator
import com.pyschool.app.navigation.Screen
import com.pyschool.app.ui.common.BaseActivity
import com.pyschool.app.ui.common.PythonHighlighter
import com.pyschool.app.ui.common.applyStatusBarPadding
import kotlinx.coroutines.launch

/**
 * หน้า 08 — Quiz
 *  - สุ่ม 10 ข้อจาก SQLite ทุกครั้งที่เข้าหน้านี้
 *  - แตะตัวเลือก = ตอบและล็อก พร้อมเฉลยสีเขียว/แดง และคำอธิบาย
 *  - ข้อสุดท้ายปุ่มเป็น Submit: บันทึกคะแนนลง SQLite แล้วไปหน้า Result
 */
class QuizActivity : BaseActivity() {

    private lateinit var binding: ActivityQuizBinding
    private var session: QuizSession? = null
    private var submitting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.root.applyStatusBarPadding()
        binding.header.headerTitle.setText(R.string.quiz_title)
        binding.header.backButton.setOnClickListener { finish() }
        binding.bottomNav.setActive(Screen.QUIZ)

        binding.btnPrevious.setOnClickListener { session?.let { it.previous(); render() } }
        binding.btnNext.setOnClickListener { onNextClicked() }

        loadQuestions()
    }

    private fun loadQuestions() {
        lifecycleScope.launch {
            val questions = app.quizRepository.getRandomQuestions()
            if (questions.isEmpty()) {
                finish()
                return@launch
            }
            session = QuizSession(questions)
            binding.quizContent.visibility = View.VISIBLE
            render()
        }
    }

    // ---------- Actions ----------

    private fun onSelect(option: Int) {
        val s = session ?: return
        if (s.isAnswered) return
        s.select(option)
        render()
    }

    private fun onNextClicked() {
        val s = session ?: return
        if (!s.isAnswered || submitting) return
        if (s.isLast) submit(s) else {
            s.next()
            render()
            binding.scroll.scrollTo(0, 0)
        }
    }

    private fun submit(s: QuizSession) {
        submitting = true
        lifecycleScope.launch {
            // บันทึกให้เสร็จก่อนเปิดหน้า Result เพื่อให้ History เห็นข้อมูลล่าสุดแน่นอน
            app.quizRepository.saveAttempt(s.score, s.total)
            AppNavigator.openQuizResult(this@QuizActivity, s.score, s.total)
        }
    }

    // ---------- Render ----------

    private fun render() {
        val s = session ?: return
        val q = s.current

        binding.questionLabel.text = buildSpannedString {
            append(getString(R.string.quiz_question_label)).append(" ")
            bold { append((s.index + 1).toString()) }
            append(" / ${s.total}")
        }
        binding.percentLabel.text = getString(R.string.percent_format, s.progressPercent)
        binding.quizProgress.progress = s.progressPercent
        binding.topicBadge.text = q.topic
        binding.questionText.text = q.question

        binding.codeContainer.removeAllViews()
        binding.codeContainer.visibility = if (q.code == null) View.GONE else View.VISIBLE
        if (q.code != null) {
            val code = BlockCodeBinding.inflate(layoutInflater, binding.codeContainer, false)
            code.codeText.text = PythonHighlighter.highlight(this, q.code)
            binding.codeContainer.addView(code.root)
        }

        renderOptions(s)
        renderFeedback(s)

        binding.btnPrevious.alpha = if (s.isFirst) 0.4f else 1f
        binding.btnPrevious.isEnabled = !s.isFirst
        binding.btnNext.alpha = if (s.isAnswered) 1f else 0.4f
        binding.btnNextLabel.setText(if (s.isLast) R.string.quiz_submit else R.string.quiz_next)
    }

    private fun renderOptions(s: QuizSession) {
        val q = s.current
        binding.optionsContainer.removeAllViews()
        q.options.forEachIndexed { i, text ->
            val item = ItemQuizOptionBinding.inflate(layoutInflater, binding.optionsContainer, false)
            item.optionText.text = text
            val isCorrect = i == q.correctIndex
            val isSelected = i == s.selectedOption

            if (s.isAnswered) {
                when {
                    isCorrect -> paint(item, R.color.success, R.color.ink, R.drawable.ic_check)
                    isSelected -> paint(item, R.color.danger, R.color.ink, R.drawable.ic_info)
                    else -> item.root.alpha = 0.55f
                }
                if (isSelected) item.radio.setBackgroundResource(R.drawable.bg_radio_on)
            }
            item.root.setOnClickListener { onSelect(i) }
            binding.optionsContainer.addView(item.root)
        }
    }

    private fun paint(item: ItemQuizOptionBinding, bg: Int, stroke: Int, icon: Int) {
        item.root.setCardBackgroundColor(ContextCompat.getColor(this, bg))
        item.root.strokeColor = ContextCompat.getColor(this, stroke)
        item.statusIcon.setImageResource(icon)
        item.statusIcon.visibility = View.VISIBLE
    }

    private fun renderFeedback(s: QuizSession) {
        binding.feedbackCard.visibility = if (s.isAnswered) View.VISIBLE else View.GONE
        if (!s.isAnswered) return
        val correct = s.selectedOption == s.current.correctIndex
        binding.feedbackTitle.setText(if (correct) R.string.quiz_correct else R.string.quiz_incorrect)
        binding.feedbackText.text = s.current.explanation
    }
}
