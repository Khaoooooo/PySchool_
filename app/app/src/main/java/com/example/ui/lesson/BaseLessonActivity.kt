package com.pyschool.app.ui.lesson

import android.os.Bundle
import androidx.core.view.doOnLayout
import com.pyschool.app.R
import com.pyschool.app.data.content.LessonContents
import com.pyschool.app.databinding.ActivityLessonBinding
import com.pyschool.app.navigation.AppNavigator
import com.pyschool.app.navigation.Screen
import com.pyschool.app.ui.common.BaseActivity
import com.pyschool.app.ui.common.applyNavigationBarPadding
import com.pyschool.app.ui.common.applyStatusBarPadding
import kotlinx.coroutines.launch

/**
 * หน้า 03–06 — โครงหน้าบทเรียนที่ใช้ร่วมกัน (Lesson 1–4 สืบทอดคลาสนี้)
 *
 * ความก้าวหน้า: วัดจากระยะที่ผู้เรียนเลื่อนอ่าน (ถึงท้ายหน้า = 100%)
 * และบันทึกลง SQLite ตอนออกจากหน้า / กด Next (ค่าจะไม่ลดลง)
 */
abstract class BaseLessonActivity(private val screen: Screen) : BaseActivity() {

    private lateinit var binding: ActivityLessonBinding
    private val lessonId = Screen.lessons.indexOf(screen) + 1
    private var maxPercent = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLessonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.applyStatusBarPadding()
        binding.content.applyNavigationBarPadding()

        bindContent()
        bindActions()
        trackReadingProgress()
    }

    override fun onPause() {
        super.onPause()
        saveProgress()
    }

    private fun bindContent() {
        val number = "%02d".format(lessonId)
        val content = LessonContents.forLesson(lessonId)
        binding.headerTitle.text = getString(R.string.lesson_title_format, number)
        binding.lessonCounter.text = getString(R.string.lesson_counter, lessonId, Screen.lessons.size)
        binding.kicker.text = getString(R.string.lesson_label, number)
        binding.title.text = content.title
        binding.subtitle.text = content.subtitle

        val renderer = LessonRenderer(this)
        content.sections.forEachIndexed { index, section ->
            binding.sections.addView(renderer.section(binding.sections, index + 1, section))
        }
    }

    private fun bindActions() {
        val isLast = screen == Screen.lessons.last()
        binding.btnNextLabel.setText(if (isLast) R.string.btn_visual_learn else R.string.btn_next_lesson)

        binding.backButton.setOnClickListener { finish() }
        binding.btnBack.setOnClickListener { AppNavigator.previousLesson(this, screen) }
        binding.btnNext.setOnClickListener {
            maxPercent = 100 // กด Next = อ่านจบบทนี้
            saveProgress()
            AppNavigator.nextLesson(this, screen)
        }
    }

    // ---------- progress ----------

    private fun trackReadingProgress() {
        binding.scroll.setOnScrollChangeListener { _, _, _, _, _ -> updateReadPercent() }
        binding.scroll.doOnLayout { updateReadPercent() }
    }

    private fun updateReadPercent() {
        val total = binding.scroll.getChildAt(0).height
        if (total == 0) return
        val seen = binding.scroll.scrollY + binding.scroll.height
        val nearBottom = seen >= total - (24 * resources.displayMetrics.density).toInt()
        val percent = if (nearBottom) 100 else seen * 100 / total
        if (percent > maxPercent) maxPercent = percent
    }

    /** ใช้ applicationScope เพราะ Activity อาจถูกปิดทันทีหลังกด Next (lifecycleScope จะถูกยกเลิก) */
    private fun saveProgress() {
        if (maxPercent == 0) return
        val percent = maxPercent
        app.applicationScope.launch { app.progressRepository.updateProgress(lessonId, percent) }
    }
}
