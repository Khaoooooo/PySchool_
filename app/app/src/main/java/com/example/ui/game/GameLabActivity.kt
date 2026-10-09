package com.pyschool.app.ui.game

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.pyschool.app.R
import com.pyschool.app.databinding.ActivityGameLabBinding
import com.pyschool.app.databinding.ItemVisualTabBinding
import com.pyschool.app.ui.common.BaseActivity
import com.pyschool.app.ui.common.PythonHighlighter
import com.pyschool.app.ui.common.applyStatusBarPadding

class GameLabActivity : BaseActivity() {

    private lateinit var binding: ActivityGameLabBinding

    private val pythonCode = """def update():
    if keyboard.right:
        player.x += 5
    if player.colliderect(star):
        score = 1"""

    private data class ConceptTab(
        val label: String,
        val title: String,
        val desc: String,
        val lineIndex: Int,
    )

    private val tabs = listOf(
        ConceptTab(
            "01 Input",
            "01 Read keyboard input",
            "เมื่อผู้เรียนกดปุ่มสั่งงาน ระบบจะตรวจจับอินพุตจากคีย์บอร์ดที่บรรทัด if keyboard.right:",
            1,
        ),
        ConceptTab(
            "02 Move",
            "02 Update player position",
            "คำสั่ง player.x += 5 จะทำการบวกพิกัดแกน X ของตัวละครให้เคลื่อนที่ไปทางขวา",
            2,
        ),
        ConceptTab(
            "03 Collision",
            "03 Check collision",
            "คำสั่ง if player.colliderect(star): จะตรวจเช็กว่าขอบของวงกลม Py ชนกับดาวแล้วหรือยัง",
            3,
        ),
        ConceptTab(
            "04 Frame",
            "04 Draw next frame",
            "เมื่อการชนสำเร็จ เงื่อนไขกลายเป็น True ระบบจะเปลี่ยนค่าคะแนน score = 1 และวาดเฟรมใหม่ทันที",
            4,
        ),
    )

    private var activeTab = 0
    private var isCollided = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameLabBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.root.applyStatusBarPadding()
        binding.header.headerTitle.setText(R.string.home_bonus_title)
        binding.header.backButton.setOnClickListener { finish() }

        setupGameControls()
        setupConceptTabs()
        renderCodeHighlight(tabs[0].lineIndex)
    }

    private fun setupGameControls() {
        binding.btnRight.setOnClickListener {
            movePlayer(deltaDp = 24)
        }
        binding.btnLeft.setOnClickListener {
            movePlayer(deltaDp = -24)
        }
        binding.btnResetGame.setOnClickListener {
            resetGame()
        }
    }

    private fun movePlayer(deltaDp: Int) {
        val density = resources.displayMetrics.density
        val deltaPx = (deltaDp * density).toInt()
        val currentMargin = (binding.playerView.layoutParams as FrameLayout.LayoutParams).leftMargin
        val maxMargin = binding.gameStage.width - binding.playerView.width - (16 * density).toInt()
        val minMargin = (12 * density).toInt()

        val newMargin = (currentMargin + deltaPx).coerceIn(minMargin, maxMargin.coerceAtLeast(minMargin))
        (binding.playerView.layoutParams as FrameLayout.LayoutParams).leftMargin = newMargin
        binding.playerView.requestLayout()

        // Check collision with star
        binding.gameStage.post {
            val playerX = binding.playerView.x + binding.playerView.width
            val starX = binding.starView.x + (binding.starView.width / 3f)

            if (!isCollided && (playerX >= starX)) {
                triggerCollisionSuccess()
            }
        }
    }

    private fun triggerCollisionSuccess() {
        isCollided = true
        binding.scoreText.text = getString(R.string.score_format, 1, 1).let { "SCORE: 01" }
        binding.missionTitle.text = "STAR COLLECTED!"
        binding.missionTitle.setTextColor(ContextCompat.getColor(this, R.color.purple_deep))
        binding.missionText.text = "Star collected! The collision condition became True and the score changed to 01."
        binding.missionCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.yellow_pale))
        
        selectConceptTab(2) // Jump to collision concept tab
    }

    private fun resetGame() {
        isCollided = false
        val density = resources.displayMetrics.density
        (binding.playerView.layoutParams as FrameLayout.LayoutParams).leftMargin = (20 * density).toInt()
        binding.playerView.requestLayout()

        binding.scoreText.text = "SCORE: 00"
        binding.missionTitle.text = "GAME MISSION"
        binding.missionTitle.setTextColor(ContextCompat.getColor(this, R.color.purple_deep))
        binding.missionText.text = "Use the > button to move Py toward the star. Watch what happens when collision detection condition evaluates to True!"
        binding.missionCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
        
        selectConceptTab(0)
    }

    private fun setupConceptTabs() {
        binding.tabsContainer.removeAllViews()
        tabs.forEachIndexed { index, tab ->
            val tabView = ItemVisualTabBinding.inflate(layoutInflater, binding.tabsContainer, false).root
            tabView.text = tab.label
            tabView.setOnClickListener { selectConceptTab(index) }
            binding.tabsContainer.addView(tabView)
        }
        selectConceptTab(0)
    }

    private fun selectConceptTab(index: Int) {
        activeTab = index
        val tabInfo = tabs[index]
        
        for (i in 0 until binding.tabsContainer.childCount) {
            val child = binding.tabsContainer.getChildAt(i) as TextView
            child.setBackgroundResource(if (i == index) R.drawable.bg_tab_selected else 0)
        }

        binding.tabConceptTitle.text = tabInfo.title
        binding.tabConceptDesc.text = tabInfo.desc
        renderCodeHighlight(tabInfo.lineIndex)
    }

    private fun renderCodeHighlight(lineIndex: Int) {
        val highlighted = PythonHighlighter.highlight(this, pythonCode)
        val text = SpannableString(highlighted)
        val lines = pythonCode.split("\n")

        if (lineIndex in lines.indices) {
            val start = lines.take(lineIndex).sumOf { it.length + 1 }
            val end = start + lines[lineIndex].length
            text.setSpan(
                BackgroundColorSpan(ContextCompat.getColor(this, R.color.code_line)),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        binding.codeText.text = text
    }
}
