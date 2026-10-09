package com.pyschool.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.pyschool.app.R
import com.pyschool.app.data.Lesson
import com.pyschool.app.data.LessonCatalog
import com.pyschool.app.data.model.LessonProgress
import com.pyschool.app.databinding.ActivityHomeBinding
import com.pyschool.app.databinding.ItemLessonCardBinding
import com.pyschool.app.databinding.ItemQuickCardBinding
import com.pyschool.app.navigation.AppNavigator
import com.pyschool.app.navigation.Screen
import com.pyschool.app.ui.common.BaseActivity
import com.pyschool.app.ui.common.applyStatusBarPadding
import com.pyschool.app.ui.common.highlightedTitle
import kotlinx.coroutines.launch

/**
 * หน้า 02 — Home / Dashboard
 *  - ความก้าวหน้ารวม = ค่าเฉลี่ยของทุกบท
 *  - รายการ 4 Core Modules + Bonus Tracks + Study Tools
 */
class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.hero.applyStatusBarPadding()
        binding.bottomNav.setActive(Screen.HOME)
        binding.heroTitle.text = buildHeroTitle()
        binding.profileButton.setOnClickListener { AppNavigator.open(this, Screen.ABOUT) }

        binding.bonusGameCard.setOnClickListener { AppNavigator.open(this, Screen.GAME_LAB) }
        binding.bonusAdvancedCard.setOnClickListener { AppNavigator.open(this, Screen.ADVANCED_PREVIEW) }

        buildQuickGrid()
    }

    override fun onResume() {
        super.onResume()
        // โหลดใหม่ทุกครั้งที่กลับมา เพื่อให้ % อัปเดตหลังเรียนจบ
        lifecycleScope.launch { render(app.progressRepository.getAll()) }
    }

    // ---------- Render ----------

    private fun render(progressList: List<LessonProgress>) {
        val progressById = progressList.associate { it.lessonId to it.progress }
        val lessons = LessonCatalog.lessons

        val overall = lessons.map { progressById[it.id] ?: 0 }.average().toInt()
        val completed = lessons.count { (progressById[it.id] ?: 0) >= 100 }

        binding.percentBadge.text = getString(R.string.percent_format, overall)
        binding.overallProgress.progress = overall
        binding.lessonsDone.text = getString(R.string.home_lessons_done, completed, lessons.size)

        val continueTarget = lessons.firstOrNull { (progressById[it.id] ?: 0) < 100 }?.screen ?: Screen.VISUAL
        binding.continueButton.setOnClickListener { AppNavigator.open(this, continueTarget) }

        binding.lessonList.removeAllViews()
        lessons.forEachIndexed { index, lesson ->
            binding.lessonList.addView(createLessonCard(index, lesson, progressById[lesson.id] ?: 0))
        }
    }

    private fun createLessonCard(index: Int, lesson: Lesson, progress: Int): View {
        val item = ItemLessonCardBinding.inflate(layoutInflater, binding.lessonList, false)
        item.iconText.text = lesson.iconText

        val iconBg = if (index % 2 == 1) R.color.yellow_pale else R.color.purple_pale
        item.iconBox.setCardBackgroundColor(ContextCompat.getColor(this, iconBg))
        item.lessonNumber.text = lesson.moduleText
        item.lessonTitle.setText(lesson.titleRes)
        item.lessonDesc.setText(lesson.descriptionRes)

        // Populate chips/tags
        item.chipsGroup.removeAllViews()
        val density = resources.displayMetrics.density
        lesson.chips.forEachIndexed { chipIdx, chipText ->
            val chip = TextView(this).apply {
                text = chipText
                textSize = 10.5f
                setPadding((8 * density).toInt(), (3 * density).toInt(), (8 * density).toInt(), (3 * density).toInt())
                val bgRes = if ((index + chipIdx) % 2 == 0) R.drawable.bg_chip_purple else R.drawable.bg_chip_pill
                setBackgroundResource(bgRes)
                setTextColor(ContextCompat.getColor(this@HomeActivity, R.color.ink))
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, (5 * density).toInt(), 0)
                }
                layoutParams = lp
            }
            item.chipsGroup.addView(chip)
        }

        item.lessonProgress.progress = progress
        item.lessonPercent.text = getString(R.string.percent_format, progress)
        item.root.setOnClickListener { AppNavigator.open(this, lesson.screen) }
        return item.root
    }

    // ---------- Study tools grid ----------

    private data class QuickTile(
        val screen: Screen,
        @DrawableRes val icon: Int,
        @StringRes val label: Int,
        @StringRes val note: Int,
        val backgroundColor: Int,
    )

    private fun buildQuickGrid() {
        val tiles = listOf(
            QuickTile(Screen.VISUAL, R.drawable.ic_play, R.string.tile_visual, R.string.tile_visual_note, R.color.purple),
            QuickTile(Screen.QUIZ, R.drawable.ic_quiz, R.string.tile_quiz, R.string.tile_quiz_note, R.color.yellow),
            QuickTile(Screen.HISTORY, R.drawable.ic_chart, R.string.tile_history, R.string.tile_history_note, R.color.white),
            QuickTile(Screen.ABOUT, R.drawable.ic_info, R.string.tile_about, R.string.tile_about_note, R.color.white),
        )
        val gap = (5.5f * resources.displayMetrics.density).toInt()
        tiles.forEach { tile ->
            val item = ItemQuickCardBinding.inflate(LayoutInflater.from(this), binding.quickGrid, false)
            item.icon.setImageResource(tile.icon)
            item.label.setText(tile.label)
            item.note.setText(tile.note)
            item.root.setCardBackgroundColor(ContextCompat.getColor(this, tile.backgroundColor))
            item.root.setOnClickListener { AppNavigator.open(this, tile.screen) }
            item.root.layoutParams = GridLayout.LayoutParams(
                GridLayout.spec(GridLayout.UNDEFINED, 1f),
                GridLayout.spec(GridLayout.UNDEFINED, 1f),
            ).apply {
                width = 0
                setMargins(gap, gap, gap, gap)
            }
            binding.quickGrid.addView(item.root)
        }
    }

    // ---------- Hero title ----------

    private fun buildHeroTitle(): CharSequence {
        val highlight = "real ideas."
        val full = getString(R.string.home_journey_title)
        return highlightedTitle(this, full, highlight)
    }
}
