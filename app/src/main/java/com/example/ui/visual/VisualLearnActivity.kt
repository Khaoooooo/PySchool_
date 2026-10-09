package com.pyschool.app.ui.visual

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.pyschool.app.R
import com.pyschool.app.data.content.VisualScenario
import com.pyschool.app.data.content.VisualScenarios
import com.pyschool.app.databinding.ActivityVisualBinding
import com.pyschool.app.databinding.ItemMemoryVarBinding
import com.pyschool.app.databinding.ItemVisualTabBinding
import com.pyschool.app.navigation.Screen
import com.pyschool.app.ui.common.BaseActivity
import com.pyschool.app.ui.common.PythonHighlighter
import com.pyschool.app.ui.common.applyStatusBarPadding

/**
 * หน้า 07 — Visual Learn
 * จำลองการทำงานของโค้ดทีละบรรทัด: ไฮไลต์บรรทัดที่รัน, แสดงตัวแปรในหน่วยความจำ และผลลัพธ์
 * ข้อมูลทั้งหมดมาจาก [VisualScenarios] (stepIndex = -1 คือก่อนกด Run)
 */
class VisualLearnActivity : BaseActivity() {

    private lateinit var binding: ActivityVisualBinding
    private val scenarios = VisualScenarios.all
    private val tabViews = mutableListOf<android.widget.TextView>()
    private var scenarioIndex = 0
    private var stepIndex = NOT_STARTED

    private val scenario: VisualScenario get() = scenarios[scenarioIndex]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisualBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.root.applyStatusBarPadding()
        binding.header.headerTitle.setText(R.string.nav_visual)
        binding.header.backButton.setOnClickListener { finish() }
        binding.bottomNav.setActive(Screen.VISUAL)

        buildTabs()
        binding.btnRun.setOnClickListener { runNextStep() }
        binding.btnReset.setOnClickListener { reset() }
        render(changedName = null)
    }

    // ---------- Tabs ----------

    private fun buildTabs() {
        scenarios.forEachIndexed { index, s ->
            val tab = ItemVisualTabBinding.inflate(layoutInflater, binding.tabs, false).root
            tab.text = s.tabLabel
            tab.setOnClickListener { selectScenario(index) }
            binding.tabs.addView(tab)
            tabViews.add(tab)
        }
    }

    private fun selectScenario(index: Int) {
        if (index == scenarioIndex) return
        scenarioIndex = index
        stepIndex = NOT_STARTED
        render(changedName = null)
    }

    // ---------- Actions ----------

    private fun runNextStep() {
        if (stepIndex >= scenario.steps.lastIndex) return
        val previous = currentMemory()
        stepIndex++
        // หาตัวแปรที่เพิ่งเปลี่ยน/เพิ่งเกิด เพื่อทำแอนิเมชันเด้ง
        val changed = currentMemory().firstOrNull { it !in previous }?.first
        render(changedName = changed)
    }

    private fun reset() {
        stepIndex = NOT_STARTED
        render(changedName = null)
    }

    private fun currentMemory() = scenario.steps.getOrNull(stepIndex)?.memory.orEmpty()

    // ---------- Render ----------

    private fun render(changedName: String?) {
        renderTabs()
        renderCode()
        renderMemory(changedName)
        renderOutputAndNote()
        renderStatusAndButton()
    }

    private fun renderTabs() {
        tabViews.forEachIndexed { index, tab ->
            tab.setBackgroundResource(if (index == scenarioIndex) R.drawable.bg_tab_selected else 0)
        }
    }

    private fun renderCode() {
        val code = scenario.code
        val text = SpannableString(PythonHighlighter.highlight(this, code))
        val activeLine = scenario.steps.getOrNull(stepIndex)?.activeLine
        if (activeLine != null) {
            val lines = code.split("\n")
            val start = lines.take(activeLine).sumOf { it.length + 1 }
            val end = start + lines[activeLine].length
            text.setSpan(
                BackgroundColorSpan(ContextCompat.getColor(this, R.color.code_line)),
                start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
        }
        binding.codeText.text = text
    }

    private fun renderMemory(changedName: String?) {
        val memory = currentMemory()
        binding.memoryGroup.removeAllViews()
        binding.memoryEmpty.visibility = if (memory.isEmpty()) View.VISIBLE else View.GONE
        memory.forEach { (name, value) ->
            val box = ItemMemoryVarBinding.inflate(layoutInflater, binding.memoryGroup, false)
            box.varName.text = name
            box.varValue.text = value
            binding.memoryGroup.addView(box.root)
            if (name == changedName) popIn(box.root)
        }
    }

    private fun popIn(view: View) {
        view.scaleX = 0.7f
        view.scaleY = 0.7f
        view.animate().scaleX(1f).scaleY(1f).setDuration(220).start()
    }

    private fun renderOutputAndNote() {
        val step = scenario.steps.getOrNull(stepIndex)
        val output = step?.output.orEmpty()
        binding.outputCard.visibility = if (output.isEmpty()) View.GONE else View.VISIBLE
        binding.outputText.text = output.joinToString("\n")
        binding.stageNote.text = step?.note ?: scenario.introNote
    }

    private fun renderStatusAndButton() {
        val total = scenario.steps.size
        val finished = stepIndex >= total - 1
        binding.statusText.text = when {
            stepIndex == NOT_STARTED -> getString(R.string.visual_status_ready)
            finished -> getString(R.string.visual_status_done)
            else -> getString(R.string.visual_status_step, stepIndex + 1, total)
        }
        binding.btnRun.isEnabled = !finished
        binding.btnRun.alpha = if (finished) 0.4f else 1f
        binding.btnRunLabel.setText(if (stepIndex == NOT_STARTED) R.string.visual_run else R.string.visual_next)
    }

    private companion object {
        const val NOT_STARTED = -1
    }
}
